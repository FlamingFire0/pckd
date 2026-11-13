package me.flamingo.pckd.backend.config

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.programs.programs
import me.flamingo.pckd.backend.serialization.commonSerializersModule
import me.flamingo.pckd.backend.serialization.json
import me.flamingo.pckd.common.filesystem.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import kotlin.io.path.readText
import kotlin.io.path.writeText

open class ConfigFile<T: Any>(
    path: Path,
    serializer: () -> KSerializer<T>,
    private val default: () -> T
): File(path) {
    private val serializer by lazy(serializer)
    private val lock = Any()

    private val _state by lazy { MutableStateFlow(load()) }
    val state: StateFlow<T> get() = _state

    val value: T get() = _state.value
    operator fun invoke(): T = value
    
    fun load(): T =
        if (isNotEmpty()) json.decodeFromString(serializer, path.readText())
        else default().also(::write)

    fun reload(): T = synchronized(lock) {
        runCatching { load() }
            .onSuccess { _state.value = it }
            .getOrElse { value }  // keep last good; log the error here
    }

    fun update(transform: (T) -> T) = synchronized(lock) {
        save(transform(value))
    }

    fun save(value: T = this.value) = synchronized(lock) {
        write(value)
        _state.value = value
    }

    private fun write(value: T) {
        ensure()
        val tmp = path.resolveSibling(path.fileName.toString() + ".tmp")
        tmp.writeText(json.encodeToString(serializer, value))
        Files.move(tmp, path, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE)
    }
    
    open fun createDefaultIfNotExists() {
        if (isNotEmpty()) return
        write(default())
    }
    
    
    fun open(rootDirectory: RootDirectory? = null) {
        programs.textProgram.run(path, rootDirectory)
    }
    
    companion object {
        inline fun <reified T: Any> configFile(
            path: Path,
            crossinline default: () -> T
        ): ConfigFile<T> = ConfigFile(
            path, 
            { commonSerializersModule.serializer<T>() },
            { default() }
        )
    }
}
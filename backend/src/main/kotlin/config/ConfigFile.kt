package me.flamingo.pckd.backend.config

import kotlinx.serialization.KSerializer
import kotlinx.serialization.serializer
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.programs.programs
import me.flamingo.pckd.backend.serialization.commonSerializersModule
import me.flamingo.pckd.backend.serialization.json
import me.flamingo.pckd.common.filesystem.File
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

open class ConfigFile<T: Any>(
    path: Path,
    serializer: () -> KSerializer<T>,
    private val default: () -> T
): File(path) {
    private var _value: T? = null
    val value: T by lazy { _value ?: reload() }

    private val serializer by lazy(serializer)
    
    fun load(): T {
        return if (isNotEmpty()) 
            json.decodeFromString(serializer, path.readText()) 
        else default().also { save(it) }
    }
    
    fun reload(): T = load().also { _value = it }
    
    fun save(value: T = this.value) {
        ensure()
        path.writeText(json.encodeToString(serializer, value))
    }
    
    operator fun invoke(): T = value
    
    open fun createDefaultIfNotExists() {
        if(isNotEmpty()) return
        save(default())
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
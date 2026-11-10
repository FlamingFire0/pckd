package me.flamingo.pckd.backend.config

import kotlinx.serialization.KSerializer
import me.flamingo.pckd.backend.serialization.json
import me.flamingo.pckd.common.filesystem.File
import java.nio.file.Path
import kotlin.io.path.readText
import kotlin.io.path.writeText

open class ConfigFile<T: Any>(
    path: Path,
    private val serializer: () -> KSerializer<T>,
    private val default: () -> T
): File(path) {
    private var _value: T? = null
    val value: T by lazy { _value ?: reload() }
    
    fun load(): T {
        return if (exists()) 
            json.decodeFromString(serializer(), path.readText()) 
        else default().also { save(it) }
    }
    
    fun reload(): T = load().also { _value = it }
    
    fun save(value: T = this.value) {
        ensure()
        path.writeText(json.encodeToString(serializer(), value))
    }
    
    open fun createDefaultIfNotExists() {
        if(exists()) return
        save(default())
    }
}
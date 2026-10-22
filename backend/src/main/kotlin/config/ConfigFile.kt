package me.flamingo.pckd.backend.config

import kotlinx.serialization.KSerializer
import me.flamingo.pckd.backend.serialization.json
import me.flamingo.pckd.common.filesystem.File
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

open class ConfigFile<T: Any>(
    path: Path,
    private val serializer: () -> KSerializer<T>,
    private val default: () -> T
): File(path) {
    var value: T = load()
    
    fun load(): T {
        return if (path.exists()) 
            json.decodeFromString(serializer(), path.readText()) 
        else default().also { save(it) }
    }
    
    fun reload() {
        value = load()
    }
    
    fun save(value: T = this.value) {
        ensure()
        path.writeText(json.encodeToString(serializer(), value))
    }
}
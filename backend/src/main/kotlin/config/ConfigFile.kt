package me.flamingo.pckd.backend.config

import kotlinx.serialization.KSerializer
import me.flamingo.pckd.backend.filesystem.File
import me.flamingo.pckd.backend.json
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.readText
import kotlin.io.path.writeText

class ConfigFile<T: Any>(
    path: Path,
    val serializer: KSerializer<T>,
    val default: () -> T
): File(path) {
    var value: T = load()
    
    fun load(): T {
        return if (path.exists()) 
            json.decodeFromString(serializer, path.readText()) 
        else default().also { save(it) }
    }
    
    fun save(value: T = this.value) {
        ensure()
        path.writeText(json.encodeToString(serializer, value))
    }
}
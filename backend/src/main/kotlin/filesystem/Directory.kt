package me.flamingo.pckd.backend.filesystem

import me.flamingo.pckd.backend.filesystem.File
import me.flamingo.pckd.backend.logger
import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.exists
import kotlin.io.path.isDirectory

open class Directory(override val path: Path) : PathHolder() {
    constructor(path: String): this(Path.of(path))
    
    init {
        if(!path.exists())
            logger.info { "Directory doesn't exist: $path" }
        else
            require(path.isDirectory()) { "Path is not a directory: $path" }
    }

    override fun ensure() { path.createDirectories() }
}
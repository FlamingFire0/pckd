package me.flamingo.pckd.common.filesystem

import me.flamingo.pckd.common.logger
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile

open class File(override val path: Path) : PathHolder() {
    constructor(path: String): this(Path.of(path))
    
    init {
        if(!path.exists())
            logger.info { "File doesn't exist: $path" }
        else 
            require(path.isRegularFile()) { "Path is not a file: $path" }
    }

    override fun ensure() { 
        if (path.exists()) return
        path.createParentDirectories()
        path.createFile()
    }
}
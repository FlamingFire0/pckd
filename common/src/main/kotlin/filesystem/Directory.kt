package me.flamingo.pckd.common.filesystem

import java.nio.file.Path
import kotlin.io.path.createDirectories
import kotlin.io.path.isDirectory

open class Directory(override val path: Path) : PathHolder() {
    constructor(path: String): this(Path.of(path))
    
    init {
        if(exists())
            require(path.isDirectory()) { "Path is not a directory: $path" }
    }

    override fun ensure() {
        if (exists()) return
        path.createDirectories() 
    }
}
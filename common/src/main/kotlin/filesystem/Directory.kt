package me.flamingo.pckd.common.filesystem

import java.nio.file.Path

open class Directory(path: Path) : PathHolder(path) {
    constructor(path: String): this(Path.of(path))
    
    init {
        if(exists())
            require(isDirectory()) { "Path is not a directory: $path" }
    }

    override fun ensure() {
        if (exists()) return
        createDirectories() 
    }
}
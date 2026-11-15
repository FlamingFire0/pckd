package me.flamingo.pckd.common.filesystem

import java.nio.file.Path

open class File(path: Path) : PathHolder(path) {
    constructor(path: String): this(Path.of(path))
    
    init {
        if(exists()) 
            require(isRegularFile()) { "Path is not a file: $path" }
    }

    override fun ensure() { 
        if (exists()) return
        createParentDirectories()
        createFile()
    }
    
    fun isValid() = exists() && isRegularFile()
    
    fun isEmpty()    = isValid() && size() == 0L
    fun isNotEmpty() = isValid() && size() != 0L
}
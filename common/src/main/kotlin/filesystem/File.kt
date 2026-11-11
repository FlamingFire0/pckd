package me.flamingo.pckd.common.filesystem

import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.createFile
import kotlin.io.path.createParentDirectories
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile

open class File(override val path: Path) : PathHolder() {
    constructor(path: String): this(Path.of(path))
    
    init {
        if(exists()) 
            require(path.isRegularFile()) { "Path is not a file: $path" }
    }

    override fun ensure() { 
        if (exists()) return
        path.createParentDirectories()
        path.createFile()
    }
    
    fun isValid() = path.exists() && path.isRegularFile()
    
    fun isEmpty()    = isValid() && Files.size(path) == 0L
    fun isNotEmpty() = isValid() && Files.size(path) != 0L
}
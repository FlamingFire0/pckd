package me.flamingo.pckd.backend.filesystem

import java.nio.file.Path

abstract class PathHolder() {
    abstract val path: Path
    abstract fun ensure()
    
    operator fun div(path: String): Path = this.path.resolve(path)
    operator fun div(path: Path): Path = this.path.resolve(path)
    
    override fun toString(): String = path.toString()
}
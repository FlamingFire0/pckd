package me.flamingo.pckd.common.filesystem

import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.notExists

abstract class PathHolder() {
    abstract val path: Path
    abstract fun ensure()
    
    operator fun div(path: String): Path = this.path.resolve(path)
    operator fun div(path: Path): Path = this.path.resolve(path)
    
    override fun toString(): String = path.toString()
    
    fun exists() = path.exists()
    fun notExists() = path.notExists()
}
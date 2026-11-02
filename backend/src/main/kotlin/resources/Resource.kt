package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Identifier
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.common.filesystem.File
import java.nio.file.Files
import kotlin.io.path.createDirectories

abstract class Resource<S: Resource<S, I>, I: ResourceInfo<I, S>>(
    key: Key,
    val rootDirectory: RootDirectory,
    val resourceDirectory: I,
) {
    var key: Key = key
        protected set
    
    fun getIdentifier(): Identifier<I, S> = Identifier(key, resourceDirectory)
    fun getFile(): File = File(key.toPath(rootDirectory, resourceDirectory))
    open fun rename(name: String) { key = key.copy(value = name) }

    abstract fun open()
    abstract fun createIfNotExists(): Boolean
    
    override fun toString(): String = key.toString()

    open fun moveTo(newKey: Key) {
        val from = getFile().path
        val to = newKey.toPath(rootDirectory, resourceDirectory)

        to.parent.createDirectories()
        Files.move(from, to)

        key = newKey
    }
}
package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Identifier
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.common.filesystem.Directory
import me.flamingo.pckd.common.filesystem.File
import me.flamingo.pckd.common.logger
import java.nio.file.Path
import kotlin.io.path.exists
import kotlin.io.path.isRegularFile
import kotlin.io.path.moveTo

abstract class Resource<S: Resource<S, I>, I: ResourceInfo<I, S>>(
    key: Key,
    val rootDirectory: RootDirectory,
    val info: I,
) {
    var key: Key = key
        protected set
    
    fun getIdentifier(): Identifier<I, S> = Identifier(key, info)
    fun getFile(): File = File(key.toPath(rootDirectory, info))
    open fun rename(name: String) { key = key.copy(value = name) }

    abstract fun open()
    abstract fun createIfNotExists(): Boolean
    
    override fun toString(): String = key.toString()

    open fun moveTo(newKey: Key): Path? {
        val newKey = info.normalize(newKey)
        
        val current = getFile().path
        val destination = newKey.toPath(rootDirectory, info)

        if(destination.exists()) {
            logger.error { "Cannot move from $current to $destination, because destination already exists." }
            return null
        }
        
        if(destination.parent.isRegularFile()) {
            logger.error { "Cannot move from $current to $destination, because parent is a file." }
            return null
        }
        
        Directory(destination.parent).ensure()
        current.moveTo(destination)

        key = newKey
        
        return destination
    }
}
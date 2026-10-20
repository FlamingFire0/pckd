package me.flamingo.pckd.backend.resources

import jdk.internal.vm.ThreadContainers.root
import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.filesystem.File
import me.flamingo.pckd.backend.key.Identifier
import me.flamingo.pckd.backend.key.Key

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

    override fun toString(): String = key.toString()
}
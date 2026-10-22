package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key
import kotlin.io.path.div
import kotlin.io.path.isDirectory
import kotlin.io.path.isRegularFile
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.relativeTo

abstract class ResourceInfo<S: ResourceInfo<S, R>,R: Resource<R, S>>(
    val directory: String,
    val extension: String,
    val resource : (Key, RootDirectory) -> R
) {
    fun gather(root: RootDirectory, baseKey: Key): List<R> {
        val directoryPath = baseKey.toPath(root, this, false)

        if(directoryPath.isDirectory()) {

            val resourceRoot = root.assets / baseKey.namespace / directory
            
            return directoryPath.listDirectoryEntries().filter { it.isRegularFile() }.map {
                resource(Key(
                    baseKey.namespace,
                    it.relativeTo(resourceRoot).toString().removeSuffix(".$extension")
                ), root)
            }
        }

        return listOf(resource(baseKey, root))
    }
    
    override fun toString(): String = directory
}
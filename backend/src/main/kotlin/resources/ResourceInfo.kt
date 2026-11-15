package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key
import kotlin.io.path.*

abstract class ResourceInfo<S: ResourceInfo<S, R>,R: Resource<R, S>>(
    val directory: String,
    val extension: String,
    val resource : (Key, RootDirectory) -> R
) {
    fun gather(
        root: RootDirectory, 
        baseKey: Key,
        key: Key = normalize(baseKey)
    ): List<R> {
        val filePath      = key.toPath(root, this)
        val directoryPath = key.toPath(root, this, false)
        
        return buildList {
            if (filePath.isRegularFile()) {
                add(resource(baseKey, root))
            }

            if (directoryPath.isDirectory()) {
                directoryPath.listDirectoryEntries()
                    .filter { it.isRegularFile() && it.extension == extension }
                    .map { resource(baseKey.resolve(it.nameWithoutExtension), root) }
                    .forEach(::add)
            }

        }
    }

    open fun normalize(key: Key): Key = key
    
    override fun toString(): String = directory
}
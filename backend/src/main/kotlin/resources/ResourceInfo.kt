package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key
import kotlin.io.path.*

abstract class ResourceInfo<S: ResourceInfo<S, R>,R: Resource<R, S>>(
    val directory: String,
    val extension: String,
    val resource : (Key, RootDirectory) -> R
) {
    fun gather(root: RootDirectory, baseKey: Key): List<R> {
        val filePath = baseKey.toPath(root, this)
        val directoryPath = baseKey.toPath(root, this, false)

        return buildList {
            // Include the original resource even when the directory exists.
            if (filePath.isRegularFile()) {
                add(resource(baseKey, root))
            }

            if (directoryPath.isDirectory()) {
                val resourceRoot = root.assets / baseKey.namespace / directory

                addAll(
                    directoryPath.listDirectoryEntries()
                        .filter { it.isRegularFile() && it.extension == extension }
                        .map {
                            resource(
                                Key(
                                    baseKey.namespace,
                                    it.relativeTo(resourceRoot)
                                        .invariantSeparatorsPathString
                                        .removeSuffix(".$extension")
                                ),
                                root
                            )
                        }
                )
            }
        }
    }

    open fun normalize(key: Key): Key = key
    
    override fun toString(): String = directory
}
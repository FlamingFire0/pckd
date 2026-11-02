package me.flamingo.pckd.backend

import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.backend.resources.Resource
import me.flamingo.pckd.backend.resources.ResourceInfo
import me.flamingo.pckd.common.logger
import kotlin.io.path.deleteIfExists
import kotlin.io.path.exists

class ResourceSlot<R : Resource<R, I>, I : ResourceInfo<I, R>>(
    key: Key,
    val rootDirectory: RootDirectory,
    val info: I,
) {
    val key: Key = info.normalize(key)
    
    val resources: MutableList<R> = info.gather(rootDirectory, key).toMutableList()

    val isMultiple get() = resources.any { it.key != key && it.getFile().exists() }
    val isSingle   get() = !isMultiple
    val isEmpty    get() = resources.none { it.getFile().exists() }

    private val leaf get() = key.value.substringAfterLast('/')
    private val dir  get() = key.toPath(rootDirectory, info, addExtension = false)

    init { normalize() }   // see the note on loading below

    fun add(name: String): R {
        val original = (resources.firstOrNull { it.key == key }
            ?: info.resource(key, rootDirectory))
            .takeIf { it.getFile().exists() }

        val target = if (original == null && isEmpty) key else key.resolve(name)

        val resource = info.resource(target, rootDirectory)

        require(!resource.getFile().exists()) {
            "$target already exists"
        }

        if (original != null) {
            require(name != leaf) { "$name is already the existing file" }

            val unpackedKey = key.resolve(leaf)
            val unpacked = info.resource(unpackedKey, rootDirectory)

            require(!unpacked.getFile().exists()) {
                "$unpackedKey already exists"
            }

            // Move the original resource AND its companion files.
            original.moveTo(unpackedKey)
            logger.info { "original = $original" }

            if (original !in resources) {
                resources += original
            }
        }

        resources.removeAll { !it.getFile().exists() }

        resource.createIfNotExists()
        resources += resource

        return resource
    }

    fun remove(resource: R) {
        resource.getFile().path.deleteIfExists()            // textures: also delete meta/aseprite
        resources -= resource
        normalize()
    }

    private fun normalize() {
        val existing = resources.filter { it.getFile().exists() }

        val original = existing.firstOrNull { it.key == key }
        val children = existing.filter { it.key != key }

        if (original != null && children.isNotEmpty()) {
            val target = key.resolve(leaf)

            require(children.none { it.key == target }) {
                "$target already exists"
            }

            original.moveTo(target)
            return
        }

        if (original != null) return

        val only = children.singleOrNull() ?: return

        if (key.toPath(rootDirectory, info).exists()) return

        only.moveTo(key)
        runCatching { dir.deleteIfExists() }
    }
}
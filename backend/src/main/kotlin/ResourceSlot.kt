package me.flamingo.pckd.backend

import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.backend.resources.Resource
import me.flamingo.pckd.backend.resources.ResourceInfo
import kotlin.io.path.*

class ResourceSlot<R : Resource<R, I>, I : ResourceInfo<I, R>>(
    baseKey: Key,
    val rootDirectory: RootDirectory,
    val info: I,
) {
    val key: Key = info.normalize(baseKey)
    val resources: MutableList<R> = info.gather(rootDirectory, key).toMutableList()
    

    fun isMultiple() = resources.any { it.key != key && it.getFile().exists() }
    fun isSingle  () = !isMultiple()
    fun isEmpty   () = resources.none { it.getFile().exists() }

    private fun getLeaf() = key.value.substringAfterLast('/')
    private fun getDirectoryPath() = key.toPath(rootDirectory, info, addExtension = false)

    init { normalize() }

    fun add(name: String): R? {
        val original = (resources.firstOrNull { it.key == key }
            ?: info.resource(key, rootDirectory))
            .takeIf { it.getFile().exists() }

        val leaf = getLeaf()
        
        val target = 
            if (original == null && isEmpty() && name == leaf) key 
            else key.resolve(name)
                
        val resource = info.resource(target, rootDirectory)
        
        
        if(resource.getFile().exists()) return null
        
        if (original != null) {
            if(name == leaf) return null

            original.moveTo(key.resolve(leaf))

            if (original !in resources) {
                resources += original
            }
        }

        resources.removeAll { !it.getFile().exists() }

        resource.createIfNotExists()
        resources += resource

        normalize()

        return resource
    }

    fun remove(resource: R) {
        resource.getFile().path.deleteIfExists()   // textures: also delete meta/aseprite
        resources -= resource
        normalize()
    }

    private fun normalize() {
        val existing = resources.filter { it.getFile().exists() }

        if(resources.isEmpty()) {
            val path = key.toPath(rootDirectory, info, addExtension = false).isDirectory()
        }

        val original = existing.firstOrNull { it.key == key }
        val children = existing.filter { it.key != key }

        if (original != null && children.isNotEmpty()) {
            val target = key.resolve(getLeaf())

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
        
        val directory = getDirectoryPath()
        if(directory.isDirectory()) {
            if(directory.listDirectoryEntries().isEmpty()) {
                runCatching { directory.deleteExisting() }
            }
        }
    }
}
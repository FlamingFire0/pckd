package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.Placeholder
import me.flamingo.pckd.backend.config.getResource
import me.flamingo.pckd.backend.key.Key
import kotlin.io.path.writeText

class ItemResource(
    key: Key, 
    rootDirectory: RootDirectory
) : JsonResource<ItemResource, ItemResource.Companion>(
    key.copy(value = normalize(key.value)), 
    rootDirectory, 
    Companion
) {
    override fun rename(name: String) {
        key = key.copy(value = normalize(name))
    }

    companion object : ResourceInfo<Companion, ItemResource>(
        "items", "json", ::ItemResource
    ) {
        fun normalize(name: String) = name.removePrefix("item/")
    }

    override fun createIfNotExists(): Boolean {
        val file = getFile()
        if(file.exists()) return false

        file.ensure()

        val text = getResource("/defaults/item/default.json")?.readText(Charsets.UTF_8) ?: return false
        file.path.writeText(Placeholder.applyAll(text, file.path, rootDirectory))

        return true
    }
}
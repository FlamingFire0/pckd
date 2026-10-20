package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key
import kotlin.text.removePrefix

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
}
package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.key.Key

class ModelResource(
    key: Key,
    rootDirectory: RootDirectory,
) : JsonResource<ModelResource, ModelResource.Companion>(key, rootDirectory, Companion) {
    companion object : ResourceInfo<Companion, ModelResource>(
        "models", "json", ::ModelResource
    )
}
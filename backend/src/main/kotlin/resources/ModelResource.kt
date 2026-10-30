package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key

class ModelResource(
    key: Key,
    rootDirectory: RootDirectory,
) : JsonResource<ModelResource, ModelResource.Companion>(key, rootDirectory, Companion) {
    override fun open() = Programs.File.value.modelProgram.run(this)
    
    fun openText() = super.open()
    
    companion object : ResourceInfo<Companion, ModelResource>(
        "models", "json", ::ModelResource
    )
}
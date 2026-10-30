package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.Placeholder
import me.flamingo.pckd.backend.config.getResource
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import kotlin.io.path.writeText

class ModelResource(
    key: Key,
    rootDirectory: RootDirectory,
) : JsonResource<ModelResource, ModelResource.Companion>(key, rootDirectory, Companion) {
    override fun open() = Programs.File.value.modelProgram.run(this)
    fun openText() = super.open()

    override fun createIfNotExists(): Boolean {
        val file = getFile()
        if(file.exists()) return false

        file.ensure()
        
        val text = getResource("/defaults/model/default.json")?.readText(Charsets.UTF_8) ?: return false
        file.path.writeText(Placeholder.applyAll(text, file.path, rootDirectory))

        return true
    }
    
    companion object : ResourceInfo<Companion, ModelResource>(
        "models", "json", ::ModelResource
    )
}
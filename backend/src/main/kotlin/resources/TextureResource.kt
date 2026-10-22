package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.common.filesystem.File
import kotlin.io.path.nameWithoutExtension

class TextureResource(
    key: Key, 
    rootDirectory: RootDirectory
) : Resource<TextureResource, TextureResource.Companion>(key, rootDirectory, Companion) {
    fun getMeta() = File("${getFile()}.mcmeta")
    
    fun getAseprite() = File(getFile().path.let { it.resolveSibling("${it.nameWithoutExtension}.aseprite") })
    
    override fun open() = Programs.File.value.imageProgram.run(this)
    
    companion object : ResourceInfo<Companion, TextureResource>(
        "textures", "png", ::TextureResource
    )
}
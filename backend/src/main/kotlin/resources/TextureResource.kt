package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.filesystem.File
import me.flamingo.pckd.backend.key.Key

class TextureResource(
    key: Key, 
    rootDirectory: RootDirectory
) : Resource<TextureResource, TextureResource.Companion>(key, rootDirectory, Companion) {
    fun getMeta() = File("${getFile()}.mcmeta")
    
    companion object : ResourceInfo<Companion, TextureResource>(
        "textures", "png", ::TextureResource
    )
}
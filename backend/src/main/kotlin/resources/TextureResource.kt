package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.getResource
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.common.filesystem.File
import kotlin.io.path.exists
import kotlin.io.path.nameWithoutExtension
import kotlin.io.path.notExists
import kotlin.io.path.writeBytes

class TextureResource(
    key: Key, 
    rootDirectory: RootDirectory
) : Resource<TextureResource, TextureResource.Companion>(key, rootDirectory, Companion) {
    fun getMeta() = File("${getFile()}.mcmeta")
    
    fun getAseprite(): File {
        val long  = getFile().path.let { it.resolveSibling("${it.nameWithoutExtension}.aseprite") }
        val short = getFile().path.let { it.resolveSibling("${it.nameWithoutExtension}.ase") }
        return File(if(long.notExists() && short.exists()) short else long)
    }
    
    
    fun openMeta()     = Programs.File.value.textProgram.run(getMeta().path, rootDirectory)
    fun openAseprite() = Programs.File.value.imageProgram.run(getAseprite().path, rootDirectory)


    override fun createIfNotExists(): Boolean {
        val file = getFile()
        if(file.exists()) return false

        file.ensure()

        val text = getResource("/defaults/texture/default.png")?.readBytes() ?: return false
        file.path.writeBytes(text)

        return true
    }
    
    override fun open() = Programs.File.value.imageProgram.run(this)
    
    companion object : ResourceInfo<Companion, TextureResource>(
        "textures", "png", ::TextureResource
    )
}
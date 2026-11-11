package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.getResource
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.common.filesystem.File
import me.flamingo.pckd.common.logger
import java.nio.file.Path
import kotlin.io.path.*

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
    
    
    fun openMeta()     = Programs.value.textProgram.run(getMeta().path, rootDirectory)
    fun openAseprite() = Programs.value.asepriteProgram.run(getAseprite().path, rootDirectory)


    override fun createIfNotExists(): Boolean {
        val file = getFile()
        if(file.exists()) return false

        file.ensure()

        val text = getResource("/defaults/texture/default.png")?.readBytes() ?: return false
        file.path.writeBytes(text)

        return true
    }
    
    override fun open() = Programs.value.imageProgram.run(this)

    override fun moveTo(newKey: Key): Path? {
        val destination = super.moveTo(newKey) ?: return null
        
        getMeta().path.let { oldMeta -> 
            if(oldMeta.notExists()) return@let
            val newMeta = destination.resolveSibling("${destination.fileName}.mcmeta")
            
            if(newMeta.exists()) {
                logger.error { "Cannot move from $oldMeta to $newMeta." }
                return@let
            }
            
            oldMeta.moveTo(newMeta)
        }
        
        getAseprite().path.let { oldAse ->
            if(oldAse.notExists()) return@let
            val newAse  = destination.resolveSibling("${destination.nameWithoutExtension}.aseprite")
            
            if(newAse.exists()) {
                logger.error { "Cannot move from $oldAse to $newAse." }
                return@let
            }
            
            oldAse.moveTo(newAse)
        }
        
        return destination
    }
    
    companion object : ResourceInfo<Companion, TextureResource>(
        "textures", "png", ::TextureResource
    )
}
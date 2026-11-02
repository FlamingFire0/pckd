package me.flamingo.pckd.backend.resources

import me.flamingo.pckd.backend.RootDirectory
import me.flamingo.pckd.backend.config.getResource
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.common.filesystem.File
import java.nio.file.Files
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

    override fun moveTo(newKey: Key) {
        val oldFile = getFile().path
        val oldMeta = getMeta().path
        val oldAse = getAseprite().path

        val newFile = newKey.toPath(rootDirectory, resourceDirectory)
        val newMeta = newFile.resolveSibling("${newFile.fileName}.mcmeta")
        val newAse = newFile.resolveSibling(
            "${newFile.nameWithoutExtension}.aseprite"
        )

        newFile.parent.createDirectories()

        super.moveTo(newKey)

        if (oldMeta.exists()) Files.move(oldMeta, newMeta)
        if (oldAse.exists()) Files.move(oldAse, newAse)
    }
    
    companion object : ResourceInfo<Companion, TextureResource>(
        "textures", "png", ::TextureResource
    )
}
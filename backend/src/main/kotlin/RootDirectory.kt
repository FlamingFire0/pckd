package me.flamingo.pckd.backend

import me.flamingo.pckd.backend.config.ConfigFile.Companion.configFile
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.backend.resources.ItemResource
import me.flamingo.pckd.backend.resources.ModelResource
import me.flamingo.pckd.backend.resources.TextureResource
import me.flamingo.pckd.common.filesystem.Directory
import me.flamingo.pckd.common.filesystem.File
import me.flamingo.pckd.common.logger
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.extension
import kotlin.io.path.isDirectory
import kotlin.io.path.listDirectoryEntries
import kotlin.io.path.walk

class RootDirectory(path: Path): Directory(path) {
    constructor(path: String) : this(Path.of(path))
    val pckd   = Directory(path / ".pckd")
    val assets = Directory(path / "assets")
    val meta   = File     (path / "pack.mcmeta")
    val icon   = File     (path / "pack.png")
    
    val groupsConfig = configFile<MutableSet<Key>>(
        pckd / "groups.json",
    ) { mutableSetOf() }

    val groups get() = groupsConfig()
    
    fun isValid() : Boolean = meta.exists()

    init {
        logger.info {
            gatherAllKeys().toString()
        }
    }

    val allKey by lazy { gatherAllKeys() }
    
    fun gatherAllKeys(): Set<Key> = buildSet {
        val namespaces = assets.path.listDirectoryEntries().filter { it.isDirectory() }


        fun MutableSet<Key>.parseDirectory(path: Path, extension: String, generalize: (String) -> String = { it }) {
            path.walk().forEach {
                if(it.isDirectory()) return@forEach
                if(it.extension != extension) return@forEach

                add(Key(it, this@RootDirectory, generalize) )
            }
        }

        namespaces.forEach { namespace ->
            val itemsDirectory    = namespace / ItemResource.directory
            val texturesDirectory = namespace / TextureResource.directory
            val modelsDirectory   = namespace / ModelResource.directory

            parseDirectory(itemsDirectory, ItemResource.extension) { value ->
                "${ItemResource.GENERIC_DIRECTORY}$value"
            }

            parseDirectory(texturesDirectory, TextureResource.extension)
            parseDirectory(modelsDirectory, ModelResource.extension)
        }
    }
}
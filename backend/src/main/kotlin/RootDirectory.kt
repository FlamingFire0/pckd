package me.flamingo.pckd.backend

import me.flamingo.pckd.backend.config.ConfigFile.Companion.configFile
import me.flamingo.pckd.backend.key.Key
import me.flamingo.pckd.common.filesystem.Directory
import me.flamingo.pckd.common.filesystem.File
import java.nio.file.Path
import kotlin.io.path.div

class RootDirectory(path: Path): Directory(path) {
    constructor(path: String) : this(Path.of(path))
    val pckd   = Directory(path / ".pckd"      )
    val assets = Directory(path / "assets"     )
    val meta   = File     (path / "pack.mcmeta")
    val icon   = File     (path / "pack.png"   )
    
    val groupsConfig = configFile<MutableSet<Key>>(
        pckd / "groups.json",
    ) { mutableSetOf() }

    val groups get() = groupsConfig()
    
    fun isValid() : Boolean = meta.exists()
}
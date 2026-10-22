package me.flamingo.pckd.backend

import me.flamingo.pckd.backend.config.ConfigFile
import me.flamingo.pckd.backend.config.Groups
import me.flamingo.pckd.common.filesystem.Directory
import me.flamingo.pckd.common.filesystem.File
import java.nio.file.Path
import kotlin.io.path.div
import kotlin.io.path.exists

class RootDirectory(path: Path): Directory(path) {
    constructor(path: String) : this(Path.of(path))
    val pckd   = Directory(path / ".pckd"      )
    val assets = Directory(path / "assets"     )
    val meta   = File     (path / "pack.mcmeta")
    val icon   = File     (path / "pack.png"   )
    
    val groups = ConfigFile(pckd / "groups.json", Groups::serializer, ::Groups)
    
    fun isValid() : Boolean = meta.path.exists()
}
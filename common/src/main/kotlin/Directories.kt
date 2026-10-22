package me.flamingo.pckd.common

import dev.dirs.ProjectDirectories
import me.flamingo.pckd.common.filesystem.Directory

object Directories {
    val projectDirectories: ProjectDirectories = ProjectDirectories.from("me", "flamingo", "pckd")
    
    val cache      : Directory by lazy { Directory(projectDirectories.cacheDir      ) }
    val config     : Directory by lazy { Directory(projectDirectories.configDir     ) }
    val data       : Directory by lazy { Directory(projectDirectories.dataDir       ) }
    val localData  : Directory by lazy { Directory(projectDirectories.dataLocalDir  ) }
    val preferences: Directory by lazy { Directory(projectDirectories.preferenceDir ) }
}
package me.flamingo.pckd.backend.config.programs

import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.config.ConfigFile
import me.flamingo.pckd.common.Directories

@Serializable
data class Programs(
    private val image: Program? = null,
    private val text:  Program? = null,
) {
    
    val imageProgram get() = image ?: Program.DEFAULT
    val textProgram  get() = text  ?: Program.DEFAULT
    
    object File : ConfigFile<Programs>(
        Directories.config / "programs.json",
        Programs::serializer,
        ::Programs
    )
}
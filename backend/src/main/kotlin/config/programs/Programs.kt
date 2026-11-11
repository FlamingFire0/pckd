package me.flamingo.pckd.backend.config.programs

import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.config.ConfigFile
import me.flamingo.pckd.common.Directories

@Serializable
class Programs private constructor(
    private val text    : Program? = null,
    private val model   : Program? = null,
    private val image   : Program? = null,
    private val aseprite: Program? = null,
) {
    val asepriteProgram get() = aseprite ?: Program.DEFAULT
    val imageProgram    get() = image    ?: aseprite ?: Program.DEFAULT
    val textProgram     get() = text     ?: Program.DEFAULT
    val modelProgram    get() = model    ?: textProgram
    
    companion object File : ConfigFile<Programs>(
        Directories.config / "programs.json",
        { Programs.serializer() },
        ::Programs
    )
}

val programs: Programs get() = Programs()
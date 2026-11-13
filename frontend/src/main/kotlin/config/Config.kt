package me.flamingo.pckd.frontend.config

import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.config.ConfigFile
import me.flamingo.pckd.common.Directories

@Serializable
class Config private constructor(
    val scale: Float? = null
) {
    companion object File : ConfigFile<Config>(
        Directories.config / "config.json",
        { Config.serializer() },
        ::Config
    )
}
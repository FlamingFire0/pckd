package me.flamingo.pckd.frontend.config

import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.config.ConfigFile
import me.flamingo.pckd.common.Directories

@Serializable
class Config private constructor(
    val scale: Float = 1f
) {
    companion object File : ConfigFile<Config>(
        Directories.config / "config.json",
        { Config.serializer() },
        ::Config
    )
}

val config: Config get() = Config()
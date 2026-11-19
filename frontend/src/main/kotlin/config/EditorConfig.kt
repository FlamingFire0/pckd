package me.flamingo.pckd.frontend.config

import androidx.compose.runtime.getValue
import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.config.ConfigFile
import me.flamingo.pckd.common.Directories
import me.flamingo.pckd.frontend.utilities.asComposeState

@Serializable
class EditorConfig private constructor(
    val scale: Float? = null
) {
    companion object File : ConfigFile<EditorConfig>(
        Directories.config / "config.json",
        { EditorConfig.serializer() },
        ::EditorConfig
    )
}
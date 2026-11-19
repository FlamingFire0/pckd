package me.flamingo.pckd.frontend.config

import androidx.compose.runtime.getValue
import androidx.compose.ui.unit.dp
import kotlinx.serialization.Serializable
import me.flamingo.pckd.backend.config.ConfigFile
import me.flamingo.pckd.common.Directories
import me.flamingo.pckd.frontend.components.PanesState
import me.flamingo.pckd.frontend.screens.editor.Editor
import me.flamingo.pckd.frontend.utilities.asComposeState

@Serializable
class Preferences private constructor(
    val editor: Editor.EditorPreferences = Editor.EditorPreferences(),
) {
    companion object File : ConfigFile<Preferences>(
        Directories.preferences / "preferences.json",
        { Preferences.serializer() },
        ::Preferences
    )
}
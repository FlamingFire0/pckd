package me.flamingo.pckd.frontend.config

import androidx.compose.runtime.getValue
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.frontend.utilities.asComposeState

object Configs {
    val editorConfig      = EditorConfig
    val preferencesConfig = Preferences
    val programsConfig    = Programs
    
    val editor      by editorConfig     .asComposeState()
    val preferences by preferencesConfig.asComposeState()
    val programs    by programsConfig   .asComposeState()

    val all = listOf(
        editorConfig,
        preferencesConfig,
        programsConfig,
    )
}
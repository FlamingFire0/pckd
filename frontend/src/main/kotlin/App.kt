package me.flamingo.pckd.frontend

import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit
import me.flamingo.pckd.backend.config.programs.Programs
import me.flamingo.pckd.common.logger
import me.flamingo.pckd.frontend.config.Config
import me.flamingo.pckd.frontend.resources.Res
import me.flamingo.pckd.frontend.resources.pckd
import me.flamingo.pckd.frontend.screens.Home
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.theming.Theme
import org.jetbrains.compose.resources.painterResource

object App {
    const val NAME = "pckd"
    
    var screen: Screen by mutableStateOf(Home)
    lateinit var window: ComposeWindow
    
    val configFiles = listOf(
        Programs,
        Config
    )
    
    @JvmStatic
    fun main(args: Array<String>) = application {
        FileKit.init(NAME)

        Theme.initialize()

        configFiles.forEach {
            it.createDefaultIfNotExists()
        }

        logger.info { Theme.density }

        Window(
            title = NAME,
            onCloseRequest = ::exitApplication,
            icon = painterResource(Res.drawable.pckd)
        ) {
            this@App.window = window

            CompositionLocalProvider(
                LocalContextMenuRepresentation provides Theme.contextMenu,
                LocalDensity provides Theme.density
            ) { 
                MaterialTheme(Theme.materialColors, Theme.typography, Theme.Shapes.shapes) {
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color    = MaterialTheme.colors.background,) {
                        key(screen) { screen.Content() }
                    }
                }
            }
        }
    }
}
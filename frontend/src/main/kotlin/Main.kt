package me.flamingo.pckd.frontend

import androidx.compose.foundation.LocalContextMenuRepresentation
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Density
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit
import me.flamingo.pckd.frontend.resources.Res
import me.flamingo.pckd.frontend.resources.pckd
import org.jetbrains.compose.resources.painterResource


fun main() = application {
    FileKit.init(appId = "pckd")
    
    val typography = jetbrainsMono()
    
    Window(
        title = "pckd",
        onCloseRequest = ::exitApplication,
        icon = painterResource(Res.drawable.pckd)
    ) {
        CompositionLocalProvider(
            LocalDensity provides Density(1.25f),
            LocalContextMenuRepresentation provides Mocha.contextMenu
        ) {
            MaterialTheme(Mocha.materialColors, typography, shapes) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color    = MaterialTheme.colors.background,
                ) {
                    val screen = App.screen
                    
                    key(screen) {
                        screen.content()
                    }
                }
            }
        }
    }
}
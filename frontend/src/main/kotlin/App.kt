package me.flamingo.pckd.frontend

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import io.github.vinceglb.filekit.FileKit
import me.flamingo.pckd.frontend.components.dialogs.ErrorDialog
import me.flamingo.pckd.frontend.config.Configs
import me.flamingo.pckd.frontend.resources.Res
import me.flamingo.pckd.frontend.resources.pckd
import me.flamingo.pckd.frontend.screens.Screen
import me.flamingo.pckd.frontend.theming.Theme
import me.flamingo.pckd.frontend.utilities.DefaultScope
import me.flamingo.pckd.frontend.utilities.ErrorHandler
import org.jetbrains.compose.resources.painterResource
import java.awt.Dimension
import javax.swing.SwingUtilities

object App {
    const val NAME = "pckd"

    init {
        Thread.setDefaultUncaughtExceptionHandler { _, e ->
            SwingUtilities.invokeLater { ErrorHandler.report(e) }
        }
    }

    @Composable
    fun ApplicationScope.App() {
        Window(
            title = NAME,
            onCloseRequest = { exit(::exitApplication) },
            icon = painterResource(Res.drawable.pckd)
        ) {
            window.minimumSize = Dimension(500, 350)
            
            Theme.Provider {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color    = MaterialTheme.colors.background
                ) {
                    Screen.Render()
                    ErrorDialog()
                }
            }
        }
    }

    fun exit(quitApp: () -> Unit) {
        Configs.all.forEach { it.save() }
        DefaultScope.cancel()
        quitApp()
    }

    @JvmStatic
    fun main(args: Array<String>) {
        FileKit.init(NAME)
        Configs.all.forEach { it.createDefaultIfNotExists() }
        application { App() }
    }
}
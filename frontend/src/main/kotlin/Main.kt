package me.flamingo.pckd.frontend

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import me.flamingo.pckd.frontend.resources.Res
import me.flamingo.pckd.frontend.resources.pckd
import org.jetbrains.compose.resources.painterResource

@Composable
fun app() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("pckd")
    }
}

fun main() = application {
    Window(
        title = "pckd",
        onCloseRequest = ::exitApplication,
        icon = painterResource(Res.drawable.pckd)
    ) { 
        MaterialTheme(colors, typography(), shapes) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = MaterialTheme.colors.background
            ) {
                app()
            }
        }


        println("compose density = " + LocalDensity.current.density)
        println("window scale    = " + window.graphicsConfiguration.defaultTransform.scaleX)
        println("screen          = " + window.graphicsConfiguration.device.iDstring)
        println("all screens     = " + java.awt.GraphicsEnvironment.getLocalGraphicsEnvironment().screenDevices
            .joinToString { it.iDstring + "=" + it.defaultConfiguration.defaultTransform.scaleX })
    }
}
package me.flamingo.pckd.frontend.utilities

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import me.flamingo.pckd.common.logger
import me.flamingo.pckd.frontend.screens.Home
import me.flamingo.pckd.frontend.screens.Screen
import javax.swing.SwingUtilities

object ErrorHandler {
    var error: Throwable? by mutableStateOf(null)
        private set

    fun report(throwable: Throwable) {
        logger.error { "Unhandled exception: ${throwable.stackTraceToString()}" }

        SwingUtilities.invokeLater {
            Screen.open(Home)
            error = throwable
        }
    }

    fun dismiss() { error = null }

    inline fun <T> guard(block: () -> T): T? = 
        try {
            block() 
        } 
        catch (e: Exception) { 
            report(e) 
            null
        }
    
    fun error(message: String) {
        guard {
            kotlin.error(message)
        }
    }
    
}
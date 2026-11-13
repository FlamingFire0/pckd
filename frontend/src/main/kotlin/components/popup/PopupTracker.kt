package me.flamingo.pckd.frontend.components.popup

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf

object PopupTracker {
    private val open = mutableStateListOf<PopupState>()
    val openCount: Int get() = open.size

    @Composable
    fun Track(state: PopupState) {
        DisposableEffect(state) {
            open.filter { !it.isInTreeOf(state) }.forEach { it.hide() }
            open += state
            
            onDispose { open -= state }
        }
    }
}
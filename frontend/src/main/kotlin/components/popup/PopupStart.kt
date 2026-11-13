package me.flamingo.pckd.frontend.components.popup

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize

fun interface PopupStart {
    fun resolve(anchor: IntRect, window: IntSize, content: IntSize): IntOffset

    companion object {
        fun at(offset: IntOffset) = PopupStart { anchor, _, _ -> anchor.topLeft + offset }

        val Center = PopupStart { _, window, content ->
            IntOffset(
                (window.width - content.width) / 2,
                (window.height - content.height) / 2,
            )
        }
    }
}


package me.flamingo.pckd.frontend.components.popup

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

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

fun startPositionProvider(start: PopupStart, drag: IntOffset = IntOffset.Zero) = object : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val d = start.resolve(anchorBounds, windowSize, popupContentSize) + drag
        return IntOffset(
            d.x.coerceIn(0, (windowSize.width  - popupContentSize.width ).coerceAtLeast(0)),
            d.y.coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0)),
        )
    }
}
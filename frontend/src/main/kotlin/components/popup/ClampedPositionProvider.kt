package me.flamingo.pckd.frontend.components.popup

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

fun clampedPositionProvider(desired: (anchor: IntRect) -> IntOffset) = object : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val d = desired(anchorBounds)
        return IntOffset(
            d.x.coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0)),
            d.y.coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0))
        )
    }
}

fun clampedPositionProvider(offset: IntOffset) = clampedPositionProvider { it.topLeft + offset }

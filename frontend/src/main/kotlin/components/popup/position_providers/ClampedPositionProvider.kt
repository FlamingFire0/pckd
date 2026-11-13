package me.flamingo.pckd.frontend.components.popup.position_providers

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider


class ClampedPositionProvider(
    val desired: (anchor: IntRect) -> IntOffset
): PopupPositionProvider {
    constructor(offset: IntOffset): this({ it.topLeft + offset })
    
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
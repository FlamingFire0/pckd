package me.flamingo.pckd.frontend.components.popup.position_providers

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider
import me.flamingo.pckd.frontend.components.popup.PopupStart

class StartPositionProvider(
    val start: PopupStart,
    val drag: IntOffset = IntOffset.Zero
): PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val d = start.resolve(anchorBounds, windowSize, popupContentSize) + drag
        return IntOffset(
            d.x.coerceIn(0, (windowSize.width - popupContentSize.width).coerceAtLeast(0)),
            d.y.coerceIn(0, (windowSize.height - popupContentSize.height).coerceAtLeast(0)),
        )
    }
}
package me.flamingo.pckd.frontend.components.tooltip

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

class TooltipPositionProvider(
    val cursor: IntOffset,
    val gap: IntOffset = IntOffset(0, 40)
): PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ): IntOffset {
        val pointer = anchorBounds.topLeft + cursor
        val maxX = (windowSize.width  - popupContentSize.width ).coerceAtLeast(0)
        val maxY = (windowSize.height - popupContentSize.height).coerceAtLeast(0)

        // Same clamping as before
        val x = (pointer.x + gap.x).coerceIn(0, maxX)
        var y = (pointer.y + gap.y).coerceIn(0, maxY)

        // If clamping pushed the tooltip under the pointer, put it above instead
        val coversPointer =
            pointer.x in x until x + popupContentSize.width &&
                    pointer.y in y until y + popupContentSize.height

        if (coversPointer) y = (pointer.y - gap.y - popupContentSize.height).coerceIn(0, maxY)

        return IntOffset(x, y)
    }
}
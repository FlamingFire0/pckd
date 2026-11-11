package me.flamingo.pckd.frontend.components.popup

import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntRect
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.window.PopupPositionProvider

object CenteredPositionProvider : PopupPositionProvider {
    override fun calculatePosition(
        anchorBounds: IntRect,
        windowSize: IntSize,
        layoutDirection: LayoutDirection,
        popupContentSize: IntSize
    ) = IntOffset(
        ((windowSize.width - popupContentSize.width) / 2).coerceAtLeast(0),
        ((windowSize.height - popupContentSize.height) / 2).coerceAtLeast(0)
    )
}
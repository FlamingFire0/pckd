package me.flamingo.pckd.frontend.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.utilities.roundToIntOffset

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

@Composable
fun Popup(
    state: PopupState = remember { PopupState() },
    modifier: Modifier = Modifier,
    minWidth: Dp = 180.dp,
    positionProvider: PopupPositionProvider = remember(state.position) { clampedPositionProvider(state.position) },
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    content: @Composable ColumnScope.(PopupState) -> Unit
) {
    if (!state.visible) return
    
    val focusRequester = remember { FocusRequester() }
    val shape = RoundedCornerShape(8.dp)

    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = state::hide,
        properties = PopupProperties(focusable = true, dismissOnClickOutside = true)
    ) {
        LaunchedEffect(focusRequester) { focusRequester.requestFocus() }

        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .widthIn(min = minWidth)
                .shadow(8.dp, shape)
                .clip(shape)
                .background(Mocha.base)
                .border(1.dp, Mocha.surface0, shape)
                .padding(4.dp)
                .then(modifier)
                .focusRequester(focusRequester)
                .onPreviewKeyEvent { event ->
                    when {
                        event.type != KeyEventType.KeyDown -> false
                        onKeyEvent(event) -> true
                        event.key == Key.Escape -> { state.hide(); true }
                        else -> false
                    }
                }
                .focusable(),
            content = { content(state) }
        )
    }
}

class PopupState(
    visible : Boolean = false,
    position: IntOffset = IntOffset.Zero,
) {
    var visible  by mutableStateOf(visible)
    var position by mutableStateOf(position)
    
    fun show() { visible = true  }
    fun hide() { visible = false }
    fun show(position: IntOffset) { this.position = position; show() }
    
    fun updatePositionFromEvent(event: PointerEvent) {
        position = event.changes.first().position.roundToIntOffset()
    }
}
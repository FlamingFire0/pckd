package me.flamingo.pckd.frontend.components.popup

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.key.*
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import me.flamingo.pckd.frontend.theming.Mocha

@Composable
fun Popup(
    modifier: Modifier = Modifier,
    minWidth: Dp = 180.dp,
    focusable: Boolean = true,
    dismissOnClickOutside: Boolean = true,
    onKeyEvent: (KeyEvent) -> Boolean = { false },
    state: PopupState = remember { PopupState() },
    positionProvider: PopupPositionProvider = remember(state.position, state.start, state.dragOffset) {
        startPositionProvider(state.start ?: PopupStart.at(state.position), state.dragOffset)
    },
    draggable: Boolean = true,
    content: @Composable ColumnScope.(PopupState) -> Unit
): PopupState {
    if (!state.visible) return state

    val focusRequester = remember { FocusRequester() }
    val shape = RoundedCornerShape(8.dp)

    Popup(
        popupPositionProvider = positionProvider,
        onDismissRequest = state::hide,
        properties = PopupProperties(focusable = focusable, dismissOnClickOutside = dismissOnClickOutside)
    ) {
        if (focusable) LaunchedEffect(focusRequester) { focusRequester.requestFocus() }

        Column(
            modifier = Modifier
                .width(IntrinsicSize.Max)
                .height(IntrinsicSize.Max)
                .widthIn(min = minWidth)
                .shadow(8.dp, shape)
                .clip(shape)
                .background(Mocha.base)
                .border(1.dp, Mocha.surface0, shape)
                .padding(4.dp)
                .then(modifier)
                .then(if(draggable) Modifier.dragPopup(state) else Modifier)
                .then(
                    if (!focusable) Modifier
                    else Modifier
                        .focusRequester(focusRequester)
                        .onPreviewKeyEvent { event ->
                            when {
                                event.type != KeyEventType.KeyDown -> false
                                onKeyEvent(event) -> true
                                event.key == Key.Escape -> { state.hide(); true }
                                else -> false
                            }
                        }
                        .focusable()
                ),
            content = { content(state) }
        )
    }

    return state
}
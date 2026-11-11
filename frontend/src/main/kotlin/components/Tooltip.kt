package me.flamingo.pckd.frontend.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.PopupPositionProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@Composable
fun Tooltip(
    text: String,
    modifier: Modifier = Modifier,
    delay: Duration = 500.milliseconds,
    enabled: Boolean = true,
    hoverable: @Composable (tooltip: String) -> Unit
) = Tooltip({ 
    Text(text, Modifier.padding(9.dp)) 
}, modifier, delay, enabled) { hoverable(text) }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Tooltip(
    content: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    delay: Duration = 500.milliseconds,
    enabled: Boolean = true,
    hoverable: @Composable () -> Unit
) {
    val popup = remember { PopupState() }
    var job     by remember { mutableStateOf<Job?>(null) }
    val scope    = rememberCoroutineScope()
    
    fun hide() {
        job?.cancel()
        popup.hide()
    }

    fun scheduleShow() {
        job?.cancel()
        if (!enabled) return

        job = scope.launch {
            delay(delay)
            popup.show()
        }
    }

    Box(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) {
                popup.updatePositionFromEvent(it)
                scheduleShow()
            }
            .onPointerEvent(PointerEventType.Move) {
                popup.updatePositionFromEvent(it)
                hide()
                scheduleShow()
            }
            .onPointerEvent(PointerEventType.Exit)  { hide() }
            .onPointerEvent(PointerEventType.Press) { hide() }
    ) {
        hoverable()

        Popup(
            state = popup,
            minWidth = 0.dp,
            focusable = false,
            dismissOnClickOutside = false,
            positionProvider = remember(popup.position) {
                TooltipPositionProvider(popup.position)
            }
        ) {
            content()
        }
    }
}

private class TooltipPositionProvider(
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

package me.flamingo.pckd.frontend.components.tooltip

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.flamingo.pckd.frontend.components.popup.Popup
import me.flamingo.pckd.frontend.components.popup.PopupState
import me.flamingo.pckd.frontend.components.popup.PopupTracker
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
    val scope = rememberCoroutineScope()
    val state = remember { TooltipState() }

    LaunchedEffect(PopupTracker.openCount) {
        state.hovered = false
        state.hide()
    }
    
    fun scheduleShow() {
        state.job?.cancel()
        if (!enabled || !state.hovered || PopupTracker.openCount > 0) return

        state.job = scope.launch {
            delay(delay)
            if (state.hovered && PopupTracker.openCount == 0) state.show()
        }
    }

    Box(
        modifier = modifier
            .onPointerEvent(PointerEventType.Enter) {
                state.hovered = true
                state.updatePositionFromEvent(it)
                scheduleShow()
            }
            .onPointerEvent(PointerEventType.Move) {
                state.hovered = true
                state.updatePositionFromEvent(it)
                state.hide()
                scheduleShow()
            }
            .onPointerEvent(PointerEventType.Exit) {
                state.hovered = false
                state.hide()
            }
            .onPointerEvent(PointerEventType.Press) {
                state.hide()
            }
    ) {
        hoverable()

        Popup(
            state = state,
            minWidth = 0.dp,
            focusable = false,
            dismissOnClickOutside = false,
            positionProvider = remember(state.position) {
                TooltipPositionProvider(state.position)
            }
        ) {
            content()
        }
    }
}

class TooltipState: PopupState() {
    var job by mutableStateOf<Job?>(null)
    var hovered by mutableStateOf(false)
    
    override fun hide() {
        job?.cancel()
        super.hide()
    }
}
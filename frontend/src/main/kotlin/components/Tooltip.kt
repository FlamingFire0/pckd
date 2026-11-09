package me.flamingo.pckd.frontend.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.round
import androidx.compose.ui.window.Popup
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun Tooltip(
    text: String,
    delay: Duration = 500.milliseconds,
    content: @Composable (tooltip: String) -> Unit
) {
    var visible by remember { mutableStateOf(false) }
    var cursor  by remember { mutableStateOf(IntOffset.Zero) }
    var job     by remember { mutableStateOf<Job?>(null) }
    val scope    = rememberCoroutineScope()

    fun hide() {
        job?.cancel()
        visible = false
    }

    fun scheduleShow() {
        job?.cancel()
        job = scope.launch {
            delay(delay)
            visible = true
        }
    }

    Box(
        modifier = Modifier
            .onPointerEvent(PointerEventType.Enter) {
                cursor = it.changes.first().position.round()
                scheduleShow()
            }
            .onPointerEvent(PointerEventType.Move) {
                cursor = it.changes.first().position.round()
                hide()           // disappears as soon as the mouse moves
                scheduleShow()   // reappears once the mouse rests again (remove this line to keep it hidden)
            }
            .onPointerEvent(PointerEventType.Exit) { hide() }
            .onPointerEvent(PointerEventType.Press) { hide() }
    ) {
        content(text)

        if (visible) {
            Popup(
                alignment = Alignment.TopStart,
                offset = cursor + IntOffset(0, 40)
            ) {
                Surface(
                    shape = Theme.Shapes.small,
                    color = Mocha.base,
                    elevation = 4.dp
                ) {
                    Text(text, modifier = Modifier.padding(6.dp))
                }
            }
        }
    }
}
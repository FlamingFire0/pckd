package me.flamingo.pckd.frontend.components.popup

import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.drag
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
import androidx.compose.ui.input.pointer.positionChange

fun Modifier.dragPopup(state: PopupState): Modifier = pointerInput(state) {
    awaitEachGesture {
        // Main pass: children run first, so a consumed press is skipped here
        val down = awaitFirstDown(requireUnconsumed = true, pass = PointerEventPass.Main)

        drag(down.id) { change ->
            val delta = change.positionChange()
            state.dragOffset += IntOffset(delta.x.roundToInt(), delta.y.roundToInt())
            change.consume()
        }
    }
}

fun Modifier.noDragPopup(): Modifier = pointerInput(Unit) {
    awaitEachGesture {
        awaitFirstDown(requireUnconsumed = false).consume()
        waitForUpOrCancellation()
    }
}
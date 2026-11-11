package me.flamingo.pckd.frontend.components.popup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.unit.IntOffset
import me.flamingo.pckd.frontend.utilities.roundToIntOffset

class PopupState(
    visible : Boolean = false,
    position: IntOffset = IntOffset.Zero,
    start   : PopupStart? = null,
) {
    var visible    by mutableStateOf(visible)
    var position   by mutableStateOf(position)
    var start      by mutableStateOf(start)
    var dragOffset by mutableStateOf(IntOffset.Zero)

    fun show() { 
        dragOffset = IntOffset.Zero
        visible = true 
    }
    
    fun show(position: IntOffset) { 
        this.position = position
        start = null;
        show() 
    }
    
    fun hide() { visible = false }
    
    fun updatePositionFromEvent(event: PointerEvent) {
        position = event.changes.first().position.roundToIntOffset()
    }
}
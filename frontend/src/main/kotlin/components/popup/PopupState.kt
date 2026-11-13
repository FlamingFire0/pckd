package me.flamingo.pckd.frontend.components.popup

import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.input.pointer.PointerEvent
import androidx.compose.ui.unit.IntOffset
import me.flamingo.pckd.frontend.utilities.roundToIntOffset

open class PopupState(
    visible : Boolean = false,
    position: IntOffset = IntOffset.Zero,
    start   : PopupStart? = null,
    val parent: PopupState? = null,
) {
    var visible    by mutableStateOf(visible)
    var position   by mutableStateOf(position)
    var start      by mutableStateOf(start)
    var dragOffset by mutableStateOf(IntOffset.Zero)

    open fun show() { 
        dragOffset = IntOffset.Zero
        visible = true 
    }
    
    open fun show(position: IntOffset) { 
        this.position = position
        start = null
        show() 
    }
    
    open fun hide() { visible = false }
    
    fun isInTreeOf(state: PopupState): Boolean = if(state === this) true else state.parent?.isInTreeOf(this) ?: false
    fun updatePositionFromEvent(event: PointerEvent) {
        position = event.changes.first().position.roundToIntOffset()
    }
    
    companion object {
        val LocalPopupState = compositionLocalOf<PopupState?> { null }
    }
}
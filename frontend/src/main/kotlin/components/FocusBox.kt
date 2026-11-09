package me.flamingo.pckd.frontend.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.*
import androidx.compose.ui.platform.LocalFocusManager


object FocusBox {
    
    lateinit var focusManager: FocusManager
    
    @Composable
    operator fun invoke(
        contentAlignment: Alignment = Alignment.TopStart,
        propagateMinConstraints: Boolean = false,
        left:  () -> Boolean = ::moveLeft,
        right: () -> Boolean = ::moveRight,
        up:    () -> Boolean = ::moveUp,
        down:  () -> Boolean = ::moveDown,
        content: @Composable BoxScope.() -> Unit
    ) {
        focusManager = LocalFocusManager.current
        
        Box(Modifier.onKeyEvent { event ->
            if (event.type != KeyEventType.KeyDown) return@onKeyEvent false
            when (event.key) {
                Key.DirectionLeft  -> left ()
                Key.DirectionRight -> right()
                Key.DirectionUp    -> up   ()
                Key.DirectionDown  -> down ()
                else -> false
            }
        },
            contentAlignment = contentAlignment,
            propagateMinConstraints = propagateMinConstraints,
            content = content
        )
    }
    
    fun move(direction: FocusDirection) = focusManager.moveFocus(direction)
    fun moveLeft()  = move(FocusDirection.Left )
    fun moveRight() = move(FocusDirection.Right)
    fun moveUp()    = move(FocusDirection.Up   )
    fun moveDown()  = move(FocusDirection.Down )
}
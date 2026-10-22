package me.flamingo.pckd.frontend.common

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

@Composable
fun panes(
    left: @Composable () -> Unit = {},
    right: @Composable () -> Unit = {},
) {


    val density = LocalDensity.current
    var sidebarWidth by remember { mutableStateOf(240.dp) }

    Row(Modifier.fillMaxSize()) {
        Column(
            Modifier
                .width(sidebarWidth)
                .fillMaxHeight()
        ) {
            left()
        }

        // Draggable divider
        Box(
            Modifier
                .width(5.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colors.onSurface.copy(alpha = 0.12f))
                .draggable(
                    orientation = Orientation.Horizontal,
                    state = rememberDraggableState { delta ->
                        sidebarWidth = (sidebarWidth + with(density) { delta.toDp() })
                            .coerceIn(160.dp, 420.dp)
                    }
                )
        )

        Column(
            Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            right()
        }
    }
}
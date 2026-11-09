package me.flamingo.pckd.frontend.components

import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import java.awt.Cursor
import kotlin.math.roundToInt

@Composable
fun Panes(
    modifier: Modifier = Modifier,
    state: PanesState = remember { PanesState() },
    minLeft:  Dp = 48.dp,
    minRight: Dp = 48.dp,
    left:  @Composable () -> Unit = {},
    right: @Composable () -> Unit = {},
    flip: Boolean = false,
) {
    val density = LocalDensity.current
    val hitWidth = 5.dp

    BoxWithConstraints(modifier.fillMaxSize()) {
        val hitPx = with(density) { hitWidth.toPx() }
        val minLeftPx = with(density) { minLeft.toPx() }
        // divider has no layout width now, so no handlePx subtraction
        val maxLeftPx = (constraints.maxWidth - with(density) { minRight.toPx() })
            .coerceAtLeast(minLeftPx)
        val widthPx = with(density) { state.left.toPx() }.coerceIn(minLeftPx, maxLeftPx)

        val currentWidth by rememberUpdatedState(widthPx)
        val limits by rememberUpdatedState(minLeftPx..maxLeftPx)

        Row(Modifier.fillMaxSize()) {
            Box(Modifier.width(with(density) { widthPx.toDp() }).fillMaxHeight().clipToBounds()) {
                if (flip) right() else left()
            }
            Box(Modifier.weight(1f).fillMaxHeight().clipToBounds()) {
                if (flip) left() else right()
            }
        }

        // Zero-width visually, but a 5dp hit area straddling the boundary, drawn on top
        Box(
            Modifier
                .offset { IntOffset((widthPx - hitPx / 2).roundToInt(), 0) }
                .width(hitWidth)
                .fillMaxHeight()
                .pointerHoverIcon(PointerIcon(Cursor(Cursor.E_RESIZE_CURSOR)))
                .pointerInput(Unit) {
                    var grab = 0f
                    detectHorizontalDragGestures(onDragStart = { grab = it.x }) { change, _ ->
                        change.consume()
                        val px = (currentWidth + change.position.x - grab)
                            .coerceIn(limits.start, limits.endInclusive)
                        state.left = with(density) { px.toDp() }
                    }
                }
        )
    }
}

@Composable
fun ScrollPane(modifier: Modifier = Modifier, content: @Composable () -> Unit) {
    val vScroll = rememberScrollState()
    val hScroll = rememberScrollState()

    BoxWithConstraints(modifier.clipToBounds()) {
        val paneWidth = maxWidth
        val paneHeight = maxHeight
        
        Box(
            Modifier
                .fillMaxSize()
                .verticalScroll(vScroll)
                .horizontalScroll(hScroll)
        ) {
            // Natural size, but never smaller than the pane (so fillMax* content still fills it)
            Box(Modifier.widthIn(min = paneWidth).width(IntrinsicSize.Max).heightIn(min = paneHeight)) { content() }
        }

        if (vScroll.maxValue > 0) {
            VerticalScrollbar(
                rememberScrollbarAdapter(vScroll),
                Modifier.align(Alignment.CenterEnd).fillMaxHeight()
            )
        }
        if (hScroll.maxValue > 0) {
            HorizontalScrollbar(
                rememberScrollbarAdapter(hScroll),
                Modifier.align(Alignment.BottomStart).fillMaxWidth()
            )
        }
    }
}

@Stable
class PanesState(initialLeft: Dp = 240.dp) {
    var left by mutableStateOf(initialLeft)
}
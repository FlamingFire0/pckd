package me.flamingo.pckd.frontend.components.contextmenu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import me.flamingo.pckd.frontend.components.Popup
import me.flamingo.pckd.frontend.components.PopupState
import me.flamingo.pckd.frontend.components.clampedPositionProvider
import me.flamingo.pckd.frontend.theming.Mocha
import kotlin.math.roundToInt

@Composable
fun ContextMenu(
    items: List<ContextAction>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val latestItems by rememberUpdatedState(items)

    var openedByKeyboard by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var keyboardNavigation by remember { mutableStateOf(false) }
    
    val popupState = remember { PopupState() } 

    fun openMenu(fromKeyboard: Boolean, position: IntOffset = IntOffset.Zero) {
        if (latestItems.isEmpty()) return

        popupState.show(position)
        
        openedByKeyboard = fromKeyboard
        selectedIndex = null
        keyboardNavigation = fromKeyboard
    }

    fun moveSelection(direction: Int) {
        selectedIndex = selectedIndex?.let { nextEnabledIndex(latestItems, it, direction) }
            ?: latestItems.indexOfFirst { it.enabled }.takeIf { it >= 0 }
        keyboardNavigation = true
    }

    fun selectBoundary(last: Boolean) {
        val index = if (last) latestItems.indexOfLast { it.enabled }
        else latestItems.indexOfFirst { it.enabled }
        selectedIndex = index.takeIf { it >= 0 }
        keyboardNavigation = true
    }

    // Menu-specific keys; Escape is handled by popup()
    fun handleMenuKey(event: KeyEvent): Boolean = when (event.key) {
        Key.DirectionDown -> { moveSelection(1); true }
        Key.DirectionUp -> { moveSelection(-1); true }
        Key.Tab -> { moveSelection(if (event.isShiftPressed) -1 else 1); true }
        Key.MoveHome -> { selectBoundary(last = false); true }
        Key.MoveEnd -> { selectBoundary(last = true); true }
        Key.Enter, Key.NumPadEnter, Key.Spacebar -> {
            selectedIndex
                ?.let(latestItems::getOrNull)
                ?.takeIf { it.enabled }
                ?.let { item ->
                    popupState.hide()
                    item.onClick()
                }
            true
        }
        else -> false
    }

    val positionProvider = remember(openedByKeyboard) {
        if (openedByKeyboard) clampedPositionProvider { IntOffset(it.left, it.bottom) }
        else clampedPositionProvider(popupState.position)
    }

    Box(
        modifier = modifier
            .onPreviewKeyEvent { event ->
                if (event.type == KeyEventType.KeyDown &&
                    (event.key == Key.Menu || (event.key == Key.F10 && event.isShiftPressed))
                ) {
                    openMenu(fromKeyboard = true)
                    true
                } else false
            }
            .focusable()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(PointerEventPass.Initial)
                        if (event.type != PointerEventType.Press) continue

                        if (event.buttons.isSecondaryPressed) {
                            val position = event.changes.firstOrNull()?.position ?: continue
                            openMenu(
                                fromKeyboard = false,
                                position = IntOffset(position.x.roundToInt(), position.y.roundToInt())
                            )
                        } else if (popupState.visible) {
                            popupState.hide()
                        }
                    }
                }
            }
    ) {
        content()
        

        if (items.isNotEmpty()) {
            Popup(
                state            = popupState,
                positionProvider = positionProvider,
                onKeyEvent       = ::handleMenuKey
            ) {
                items.forEachIndexed { index, item ->
                    ContextMenuRow(
                        action   = item,
                        selected = index == selectedIndex,
                        keyboardNavigation = keyboardNavigation,
                        onHover = { hovered ->
                            if (hovered) {
                                selectedIndex = index
                                keyboardNavigation = false
                            } else if (!keyboardNavigation && selectedIndex == index) {
                                selectedIndex = null
                            }
                        },
                        onClick = {
                            popupState.hide()
                            item.onClick()
                        }
                    )
                }
            }
        }
    }
}

private fun nextEnabledIndex(
    items: List<ContextAction>,
    currentIndex: Int,
    direction: Int
): Int? {
    if (items.isEmpty()) return null

    for (distance in 1..items.size) {
        val index = Math.floorMod(
            currentIndex + direction * distance,
            items.size
        )

        if (items[index].enabled) {
            return index
        }
    }

    return null
}

@Composable
private fun ContextMenuRow(
    action: ContextAction,
    selected: Boolean,
    keyboardNavigation: Boolean,
    onHover: (Boolean) -> Unit,
    onClick: () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val hovered by interactionSource.collectIsHoveredAsState()
    val latestOnHover by rememberUpdatedState(onHover)
    val shape = RoundedCornerShape(4.dp)

    LaunchedEffect(hovered) {
        latestOnHover(hovered)
    }

    val highlighted = if (keyboardNavigation) {
        selected
    } else {
        hovered
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(
                if (action.enabled && highlighted) {
                    Mocha.surface0
                } else {
                    Color.Transparent
                }
            )
            .hoverable(
                interactionSource = interactionSource,
                enabled = action.enabled
            )
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                enabled = action.enabled,
                onClick = onClick
            )
            .padding(horizontal = 12.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = action.label,
            fontSize = 14.sp,
            color = if (action.enabled) Mocha.text else Mocha.overlay0,
            maxLines = 1
        )
    }
}
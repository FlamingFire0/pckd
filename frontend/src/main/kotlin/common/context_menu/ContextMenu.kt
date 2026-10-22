package me.flamingo.pckd.frontend.common.context_menu

import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.*
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.isSecondaryPressed
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.*
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupPositionProvider
import androidx.compose.ui.window.PopupProperties
import me.flamingo.pckd.frontend.Mocha
import kotlin.math.roundToInt

@Composable
fun contextMenu(
    items: List<ContextAction>,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    val latestItems by rememberUpdatedState(items)

    var expanded by remember { mutableStateOf(false) }
    var clickPosition by remember { mutableStateOf(IntOffset.Zero) }
    var openedByKeyboard by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableStateOf<Int?>(null) }
    var keyboardNavigation by remember { mutableStateOf(false) }

    val isExpanded by rememberUpdatedState(expanded)
    val menuFocusRequester = remember { FocusRequester() }

    fun openMenu(
        fromKeyboard: Boolean,
        position: IntOffset = IntOffset.Zero
    ) {
        if (latestItems.isEmpty()) return

        clickPosition = position
        openedByKeyboard = fromKeyboard
        selectedIndex = null
        keyboardNavigation = fromKeyboard
        expanded = true
    }

    fun moveSelection(direction: Int) {
        val available = latestItems

        selectedIndex = if (selectedIndex == null) {
            // The first navigation key always selects the first enabled item.
            available.indexOfFirst { it.enabled }
                .takeIf { it >= 0 }
        } else {
            nextEnabledIndex(
                items = available,
                currentIndex = selectedIndex!!,
                direction = direction
            )
        }

        keyboardNavigation = true
    }

    fun selectBoundary(last: Boolean) {
        val available = latestItems

        val index = if (last) {
            available.indexOfLast { it.enabled }
        } else {
            available.indexOfFirst { it.enabled }
        }

        selectedIndex = index.takeIf { it >= 0 }
        keyboardNavigation = true
    }

    val positionProvider = remember(clickPosition, openedByKeyboard) {
        object : PopupPositionProvider {
            override fun calculatePosition(
                anchorBounds: IntRect,
                windowSize: IntSize,
                layoutDirection: LayoutDirection,
                popupContentSize: IntSize
            ): IntOffset {
                val desired = if (openedByKeyboard) {
                    IntOffset(
                        anchorBounds.left,
                        anchorBounds.bottom
                    )
                } else {
                    anchorBounds.topLeft + clickPosition
                }

                return IntOffset(
                    x = desired.x.coerceIn(
                        0,
                        (windowSize.width - popupContentSize.width)
                            .coerceAtLeast(0)
                    ),
                    y = desired.y.coerceIn(
                        0,
                        (windowSize.height - popupContentSize.height)
                            .coerceAtLeast(0)
                    )
                )
            }
        }
    }

    Box(
        modifier = modifier
            .onPreviewKeyEvent { event ->
                if (event.type != KeyEventType.KeyDown) {
                    false
                } else if (
                    event.key == Key.Menu ||
                    (event.key == Key.F10 && event.isShiftPressed)
                ) {
                    openMenu(fromKeyboard = true)
                    true
                } else {
                    false
                }
            }
            .focusable()
            .pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent(
                            PointerEventPass.Initial
                        )

                        if (event.type != PointerEventType.Press) {
                            continue
                        }

                        if (event.buttons.isSecondaryPressed) {
                            // Only open on an actual secondary-button press.
                            val position =
                                event.changes.firstOrNull()?.position
                                    ?: continue

                            openMenu(
                                fromKeyboard = false,
                                position = IntOffset(
                                    position.x.roundToInt(),
                                    position.y.roundToInt()
                                )
                            )
                        } else if (isExpanded) {
                            // Do not consume this event: the original window's
                            // clicked component should still receive the click.
                            expanded = false
                        }
                    }
                }
            }
    ) {
        content()

        if (expanded && items.isNotEmpty()) {
            Popup(
                popupPositionProvider = positionProvider,
                onDismissRequest = { expanded = false },
                properties = PopupProperties(
                    focusable = true,
                    dismissOnClickOutside = true
                )
            ) {
                LaunchedEffect(menuFocusRequester) {
                    menuFocusRequester.requestFocus()
                }

                val shape = RoundedCornerShape(8.dp)

                Column(
                    modifier = Modifier
                        .width(IntrinsicSize.Max)
                        .widthIn(min = 180.dp)
                        .shadow(8.dp, shape)
                        .clip(shape)
                        .background(Mocha.base)
                        .border(1.dp, Mocha.mantle, shape)
                        .padding(4.dp)
                        .focusRequester(menuFocusRequester)
                        .onPreviewKeyEvent { event ->
                            if (event.type != KeyEventType.KeyDown) {
                                false
                            } else {
                                when (event.key) {
                                    Key.DirectionDown -> {
                                        moveSelection(1)
                                        true
                                    }

                                    Key.DirectionUp -> {
                                        moveSelection(-1)
                                        true
                                    }

                                    Key.Tab -> {
                                        moveSelection(
                                            if (event.isShiftPressed) -1 else 1
                                        )
                                        true
                                    }

                                    Key.MoveHome -> {
                                        selectBoundary(last = false)
                                        true
                                    }

                                    Key.MoveEnd -> {
                                        selectBoundary(last = true)
                                        true
                                    }

                                    Key.Enter,
                                    Key.NumPadEnter,
                                    Key.Spacebar -> {
                                        // Activation requires an enabled,
                                        // currently highlighted item.
                                        selectedIndex
                                            ?.let(latestItems::getOrNull)
                                            ?.takeIf { it.enabled }
                                            ?.let { item ->
                                                expanded = false
                                                item.onClick()
                                            }

                                        true
                                    }

                                    Key.Escape -> {
                                        expanded = false
                                        true
                                    }

                                    else -> false
                                }
                            }
                        }
                        .focusable()
                ) {
                    items.forEachIndexed { index, item ->
                        contextMenuRow(
                            action = item,
                            selected = index == selectedIndex,
                            keyboardNavigation = keyboardNavigation,
                            onHover = { hovered ->
                                if (hovered) {
                                    selectedIndex = index
                                    keyboardNavigation = false
                                } else if (
                                    !keyboardNavigation &&
                                    selectedIndex == index
                                ) {
                                    selectedIndex = null
                                }
                            },
                            onClick = {
                                expanded = false
                                item.onClick()
                            }
                        )
                    }
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
private fun contextMenuRow(
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
                    Mocha.surface1
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
            fontSize = 13.sp,
            color = if (action.enabled) Mocha.text else Mocha.overlay0,
            maxLines = 1
        )
    }
}
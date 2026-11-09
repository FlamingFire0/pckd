package me.flamingo.pckd.frontend.utilities

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt

fun Offset.roundToIntOffset() = IntOffset(x.roundToInt(), y.roundToInt())
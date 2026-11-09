package me.flamingo.pckd.frontend.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.theming.Mocha

@Composable
fun Grouping(
    padding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (ColumnScope.() -> Unit)
) {
    
    val shape = RoundedCornerShape(6.dp)

    Surface(
        shape = shape,
        color = Mocha.base,
        modifier = Modifier.width(IntrinsicSize.Max).border(width = 1.dp, color = Mocha.surface0, shape)
    ) {
        Column(
            modifier = Modifier.padding(padding),
            content = content
        )
    }
}
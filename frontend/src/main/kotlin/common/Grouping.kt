package me.flamingo.pckd.frontend.common

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.Mocha

@Composable
fun grouping(
    padding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (ColumnScope.() -> Unit)
) {

    Surface(
        shape = RoundedCornerShape(6.dp),
        color = Mocha.mantle,
        modifier = Modifier.width(IntrinsicSize.Max)
    ) {
        Column(
            modifier = Modifier.padding(padding),
            content = content
        )
    }
}
package me.flamingo.pckd.frontend.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.theming.Mocha
import me.flamingo.pckd.frontend.theming.Theme

@Composable
fun SmallButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    interactionSource: MutableInteractionSource? = null,
    elevation: ButtonElevation? = null,
    shape: Shape = Theme.Shapes.medium,
    borderStroke: BorderStroke? = null,
    backgroundColor: Color = Mocha.mantle,
    buttonColors: ButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = backgroundColor,
        contentColor = Mocha.text,
        disabledBackgroundColor = Mocha.base,
        disabledContentColor = Mocha.overlay1
    ),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    content: @Composable (RowScope.() -> Unit)
) {
    Button(
        onClick, 
        modifier, 
        enabled, 
        interactionSource,
        elevation, 
        shape,
        borderStroke,
        buttonColors,
        contentPadding,
        content
    )
}
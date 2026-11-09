package me.flamingo.pckd.frontend.theming

import androidx.compose.foundation.DefaultContextMenuRepresentation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.theming.Mocha.base
import me.flamingo.pckd.frontend.theming.Mocha.mantle
import me.flamingo.pckd.frontend.theming.Mocha.maroon
import me.flamingo.pckd.frontend.theming.Mocha.mauve
import me.flamingo.pckd.frontend.theming.Mocha.overlay0
import me.flamingo.pckd.frontend.theming.Mocha.peach
import me.flamingo.pckd.frontend.theming.Mocha.red
import me.flamingo.pckd.frontend.theming.Mocha.surface0
import me.flamingo.pckd.frontend.theming.Mocha.text
import me.flamingo.pckd.frontend.theming.Mocha.yellow

object Theme {
    var fontFamily: FontFamily = FontFamily.Monospace
        private set

    val typography: Typography by lazy { Typography(fontFamily) }

    lateinit var textFieldColors: TextFieldColors
        private set

    var density = Density(1.25f)
    
    val shapes = Shapes(
        small  = RoundedCornerShape(2.dp),
        medium = RoundedCornerShape(4.dp),
        large  = RoundedCornerShape(6.dp),
    )

    val materialColors = darkColors(
        primary          = red,
        primaryVariant   = maroon,
        secondary        = peach,
        secondaryVariant = yellow,
        background       = mantle,
        surface          = surface0,
        error            = mauve,
        onPrimary        = base,
        onSecondary      = base,
        onBackground     = text,
        onSurface        = text,
        onError          = base,
    )

    val contextMenu = DefaultContextMenuRepresentation(
        backgroundColor = base,
        textColor = text,
        itemHoverColor = surface0,
        disabledTextColor = overlay0
    )

    @Composable
    private fun textFieldColors() = TextFieldDefaults.outlinedTextFieldColors(
        textColor            = text,
        cursorColor          = red,
        backgroundColor      = base,
        focusedBorderColor   = red,
        unfocusedBorderColor = surface0,
        placeholderColor     = overlay0,
    )
    
    @Composable
    fun initialize() {
        fontFamily      = jetbrainsMono()
        textFieldColors = textFieldColors()
    }
}

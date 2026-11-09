package me.flamingo.pckd.frontend.theming

import androidx.compose.foundation.DefaultContextMenuRepresentation
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import me.flamingo.pckd.frontend.utilities.platform.isX11

object Theme {
    var fontFamily: FontFamily = FontFamily.Monospace
        private set

    var typography: Typography = Typography(fontFamily)
        private set

    lateinit var textFieldColors: TextFieldColors
        private set

    var density = Density(1f)
    
    object Shapes {
        val tiny    = RoundedCornerShape( 2.dp)
        val small   = RoundedCornerShape( 4.dp)
        val medium  = RoundedCornerShape( 6.dp)
        val big     = RoundedCornerShape( 8.dp)
        val large   = RoundedCornerShape(10.dp)
        
        val shapes = Shapes(tiny, small, medium)
    }

    val materialColors = darkColors(
        primary          = Mocha.red,
        primaryVariant   = Mocha.maroon,
        secondary        = Mocha.peach,
        secondaryVariant = Mocha.yellow,
        background       = Mocha.crust,
        surface          = Mocha.surface0,
        error            = Mocha.mauve,
        onPrimary        = Mocha.crust,
        onSecondary      = Mocha.crust,
        onBackground     = Mocha.text,
        onSurface        = Mocha.text,
        onError          = Mocha.crust,
    )

    val contextMenu = DefaultContextMenuRepresentation(
        backgroundColor   = Mocha.base,
        textColor         = Mocha.text,
        itemHoverColor    = Mocha.surface0,
        disabledTextColor = Mocha.overlay0
    )

    @Composable
    fun textFieldColors(accent: Color = Mocha.red) = TextFieldDefaults.outlinedTextFieldColors(
        textColor            = Mocha.text,
        cursorColor          = Mocha.subtext0,
        backgroundColor      = Mocha.base,
        focusedBorderColor   = accent,
        unfocusedBorderColor = Mocha.surface0,
        placeholderColor     = Mocha.overlay0,
    )
    
    @Composable
    fun initialize() {
        fontFamily      = jetbrainsMono()
        typography      = Typography(fontFamily)
        textFieldColors = textFieldColors()
        density = when {
            isX11 -> Density(1.25f)
            else  -> LocalDensity.current
        }
    }
}

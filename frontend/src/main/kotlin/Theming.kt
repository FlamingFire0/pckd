package me.flamingo.pckd.frontend

import androidx.compose.foundation.DefaultContextMenuRepresentation
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import me.flamingo.pckd.frontend.resources.*
import org.jetbrains.compose.resources.Font

object Mocha {
    val rosewater = Color(0xfff5e0dc)
    val flamingo  = Color(0xfff2cdcd)
    val pink      = Color(0xfff5c2e7)
    val mauve     = Color(0xffcba6f7)
    val red       = Color(0xfff38ba8)
    val maroon    = Color(0xffeba0ac)
    val peach     = Color(0xfffab387)
    val yellow    = Color(0xfff9e2af)
    val green     = Color(0xffa6e3a1)
    val teal      = Color(0xff94e2d5)
    val sky       = Color(0xff89dceb)
    val sapphire  = Color(0xff74c7ec)
    val blue      = Color(0xff89b4fa)
    val lavender  = Color(0xffb4befe)
    
    val text      = Color(0xffcdd6f4)
    val subtext1  = Color(0xffbac2de)
    val subtext0  = Color(0xffa6adc8)
    val overlay2  = Color(0xff9399b2)
    val overlay1  = Color(0xff7f849c)
    val overlay0  = Color(0xff6c7086)
    val surface2  = Color(0xff585b70)
    val surface1  = Color(0xff45475a)
    val surface0  = Color(0xff313244)
    val base      = Color(0xff1e1e2e)
    val mantle    = Color(0xff181825)
    val crust     = Color(0xff11111b)
    
    
    val materialColors = darkColors(
        primary          = red,
        primaryVariant   = maroon,
        secondary        = peach,
        secondaryVariant = yellow,
        background       = base,
        surface          = surface0,
        error            = mauve,
        onPrimary        = base,
        onSecondary      = base,
        onBackground     = text,
        onSurface        = text,
        onError          = base,
    )

    val contextMenu = 
        DefaultContextMenuRepresentation(
            backgroundColor = base,
            textColor = text,
            itemHoverColor = surface0,
            disabledTextColor = overlay0
        )
}

val shapes = Shapes()

@Composable
fun jetbrainsMono() = Typography(
    FontFamily(
        Font(Res.font.thin,               FontWeight.Thin,                       ),
        Font(Res.font.extra_light,        FontWeight.ExtraLight,                 ),
        Font(Res.font.light,              FontWeight.Light,                      ),
        Font(Res.font.regular,            FontWeight.Normal,                     ),
        Font(Res.font.medium,             FontWeight.Medium,                     ),
        Font(Res.font.semi_bold,          FontWeight.SemiBold,                   ),
        Font(Res.font.bold,               FontWeight.Bold,                       ),
        Font(Res.font.extra_bold,         FontWeight.ExtraBold,                  ),
        Font(Res.font.thin_italic,        FontWeight.Thin,       FontStyle.Italic),
        Font(Res.font.extra_light_italic, FontWeight.ExtraLight, FontStyle.Italic),
        Font(Res.font.light_italic,       FontWeight.Light,      FontStyle.Italic),
        Font(Res.font.italic,             FontWeight.Normal,     FontStyle.Italic),
        Font(Res.font.medium_italic,      FontWeight.Medium,     FontStyle.Italic),
        Font(Res.font.semi_bold_italic,   FontWeight.SemiBold,   FontStyle.Italic),
        Font(Res.font.bold_italic,        FontWeight.Bold,       FontStyle.Italic),
        Font(Res.font.extra_bold_italic,  FontWeight.ExtraBold,  FontStyle.Italic),
    )
)
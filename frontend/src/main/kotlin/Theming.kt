package me.flamingo.pckd.frontend

import androidx.compose.material.Colors
import androidx.compose.material.Shapes
import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import me.flamingo.pckd.frontend.resources.*
import org.jetbrains.compose.resources.Font

val colors = Colors(
    primary          = Color(0xFFF38BA8), // Red
    primaryVariant   = Color(0xFFEBA0AC), // Maroon
    secondary        = Color(0xFFFAB387), // Peach
    secondaryVariant = Color(0xFFF9E2AF), // Yellow
    background       = Color(0xFF1E1E2E), // Base
    surface          = Color(0xFF313244), // Surface0
    error            = Color(0xFFCBA6F7), // Mauve
    onPrimary        = Color(0xFF1E1E2E), // Base
    onSecondary      = Color(0xFF1E1E2E), // Base
    onBackground     = Color(0xFFCDD6F4), // Text
    onSurface        = Color(0xFFCDD6F4), // Text
    onError          = Color(0xFF1E1E2E), // Base
    
    isLight          = false
)

val shapes = Shapes()

@Composable
fun typography() = Typography(
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
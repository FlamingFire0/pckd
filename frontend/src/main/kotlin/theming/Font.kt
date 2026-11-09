package me.flamingo.pckd.frontend.theming

import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import me.flamingo.pckd.frontend.resources.*
import org.jetbrains.compose.resources.Font

@Composable
fun jetbrainsMono() = FontFamily(
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
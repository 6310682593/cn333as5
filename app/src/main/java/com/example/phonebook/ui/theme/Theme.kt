package com.example.phonebook.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.phonebook.util.fromHex

private val DarkColorPalette = darkColors(
    primary = Color.fromHex("#9E9EA8"),
    primaryVariant = Color.fromHex("#FCEED1"),
    secondary = Color.fromHex("#FCEED1")
)

private val LightColorPalette = lightColors(
    primary = Color.fromHex("#9DB6CF"),
    primaryVariant = Color.fromHex("#DBD2CF"),
    secondary = Color.fromHex("#DBD2CF")

    /* Other default colors to override
    background = Color.White,
    surface = Color.White,
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onBackground = Color.Black,
    onSurface = Color.Black,
    */
)

@Composable
fun PhoneBookTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}

object PhoneBookThemeSettings {
    var isDarkThemeEnabled by mutableStateOf(false)
}
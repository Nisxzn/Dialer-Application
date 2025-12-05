package com.example.smartdialer.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColors = darkColorScheme(
    primary = Color.White,
    onPrimary = Color.Black,

    background = Color(0xFF000000),
    onBackground = Color(0xFFFFFFFF),

    surface = Color(0xFF000000),
    onSurface = Color(0xFFFFFFFF),

    secondary = Color(0xFF000000),
    onSecondary = Color.White
)

@Composable
fun NothingTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColors,
        typography = NothingTypography,
        content = content
    )
}

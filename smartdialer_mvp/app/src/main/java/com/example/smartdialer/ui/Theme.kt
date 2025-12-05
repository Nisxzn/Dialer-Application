package com.example.smartdialer.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

private val NothingBlackColors: ColorScheme = darkColorScheme(
    primary = Color(0xFFFFFFFF),
    onPrimary = Color.Black,
    background = Color(0xFF000000),
    surface = Color(0xFF050505),
    onSurface = Color(0xFFFFFFFF)
)

private val NothingTypography = Typography(
    titleLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 28.sp,
        fontFamily = FontFamily.SansSerif
    ),
    bodyLarge = androidx.compose.ui.text.TextStyle(
        fontSize = 16.sp,
        fontFamily = FontFamily.SansSerif
    )
)

@Composable
fun NothingTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = NothingBlackColors,
        typography = NothingTypography,
        content = content
    )
}

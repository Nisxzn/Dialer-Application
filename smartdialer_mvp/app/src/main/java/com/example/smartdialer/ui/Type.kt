package com.example.smartdialer.ui

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp

// you will load dot-matrix font in assets and use it in DotNumber composable
val NothingTypography = Typography(
    displayLarge = TextStyle(fontFamily = FontFamily.Monospace, fontSize = 36.sp),
    titleLarge = TextStyle(fontSize = 20.sp),
    bodyLarge = TextStyle(fontSize = 16.sp)
)

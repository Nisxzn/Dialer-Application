package com.example.smartdialer.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.smartdialer_mvp.R

@Composable
fun DotNumber(value: String, placeholder: String = "+91 123 456 7890") {
    // load DotMatrix.ttf from assets via resource if you placed it in res/font/dotmatrix.ttf
    // fallback: use monospace
    val family = try {
        FontFamily(Font(resId = R.font.dotmatrix))
    } catch (e: Exception) {
        FontFamily.Monospace
    }

    Box(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = if (value.isNotEmpty()) value else placeholder,
            fontFamily = family,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

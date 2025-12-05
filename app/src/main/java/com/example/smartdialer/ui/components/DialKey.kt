package com.example.smartdialer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit

@Composable
fun DialKey(label: String,size: Dp,fontSize: TextUnit, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(size)
            .background(Color(0xFF0B0B0B), shape = RoundedCornerShape(16.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = label, color = Color.White,fontSize = fontSize, style = MaterialTheme.typography.titleLarge)
    }
}

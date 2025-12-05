package com.example.smartdialer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Nothing Phone style dialer key:
 * - Rounded octagon look
 * - Soft inner glow effect
 * - Matte black layered shading
 */
@Composable
fun NothingKey(
    key: String,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(86.dp) // Exact Nothing dialer key size
            .clip(RoundedCornerShape(26.dp)) // Soft octagon-ish corners
            .background(Color(0xFF0E0E0E)) // Deep matte black
            .border(
                width = 1.dp,
                color = Color(0x22FFFFFF), // Soft thin outline
                shape = RoundedCornerShape(26.dp)
            )
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = key,
            color = Color.White,
            fontSize = 22.sp
        )
    }
}

package com.example.smartdialer.ui.screens

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdialer.Contact
import com.example.smartdialer.loadAllContacts
import com.example.smartdialer.ui.components.DialKey
import com.example.smartdialer_mvp.MainActivity

@Composable
fun DialerScreen(
    navToContacts: () -> Unit,
    navToRecents: () -> Unit,
    openKeypad: () -> Unit
) {
    val ctx = LocalContext.current
    var number by remember { mutableStateOf("") }
    var digits by remember { mutableStateOf("") }
    var allContacts by remember { mutableStateOf(listOf<Contact>()) }
    var suggestions by remember { mutableStateOf(listOf<Contact>()) }

    LaunchedEffect(Unit) {
        allContacts = loadAllContacts(ctx)
    }

    LaunchedEffect(digits) {
        suggestions =
            if (digits.isBlank()) emptyList()
            else allContacts.filter {
                it.name.contains(digits, ignoreCase = true) ||
                        it.number.contains(digits)
            }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 16.dp)
    ) {

        // -------------------------------------------------------------
        // TOP AREA (SCROLLABLE)
        // -------------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)                 // ← IMPORTANT: top content scrolls
                .verticalScroll(rememberScrollState())
        ) {

            // HEADER
            Text(
                text = "Dial",
                color = Color.White,
                fontSize = 42.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(top = 50.dp, bottom = 20.dp)
            )

            // INPUT
            BasicTextField(
                value = number,
                onValueChange = {
                    number = it
                    digits = it.filter { c -> c.isDigit() }
                },
                cursorBrush = SolidColor(Color.White),
                textStyle = TextStyle(
                    fontSize = 40.sp,
                    color = Color.White,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            )

            // SUGGESTIONS
            if (suggestions.isNotEmpty()) {
                Column(Modifier.padding(top = 10.dp)) {
                    suggestions.take(3).forEach { contact ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 8.dp)
                                .clickable {
                                    number = contact.number
                                    digits = number.filter { it.isDigit() }
                                },
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = contact.name,
                                color = Color.White,
                                fontSize = 22.sp,
                                modifier = Modifier.weight(1f)
                            )
                            Text(
                                text = contact.number,
                                color = Color.LightGray,
                                fontSize = 18.sp
                            )
                        }
                    }
                }
            }
        }

        // -------------------------------------------------------------
        // FIXED KEYPAD AT BOTTOM (STAYS LIKE 1st SCREENSHOT)
        // -------------------------------------------------------------
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 90.dp), // space above footer
            contentAlignment = Alignment.BottomCenter
        ) {

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()

            ) {

                val rows = listOf(
                    listOf("1", "2", "3"),
                    listOf("4", "5", "6"),
                    listOf("7", "8", "9"),
                    listOf("*", "0", "⌫")
                )

                rows.forEach { row ->
                    Row(
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {

                        row.forEach { key ->

                            if (key == "⌫") {
                                Box(
                                    modifier = Modifier
                                        .size(65.dp)
                                        .clip(RoundedCornerShape(20.dp))
                                        .background(Color(0xFF111111))
                                        .clickable {
                                            if (number.isNotEmpty()) number = number.dropLast(1)
                                            digits = number.filter { it.isDigit() }
                                        },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        Icons.Default.KeyboardArrowLeft,
                                        contentDescription = "Delete",
                                        tint = Color.White,
                                        modifier = Modifier.size(35.dp)
                                    )
                                }
                            } else {
                                DialKey(
                                    label = key,
                                    size = 68.dp,
                                    fontSize = 28.sp
                                ) {
                                    number += key
                                    if (key.first().isDigit()) digits += key
                                }
                            }
                        }
                    }
                }

                Spacer(Modifier.height(8.dp))

                // CALL BUTTON
                val activity = ctx as? MainActivity
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF4CAF50))
                        .clickable {
                            if (number.isNotBlank()) activity?.placeCall(number)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Call,
                        contentDescription = "Call",
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
            }
        }
    }
}

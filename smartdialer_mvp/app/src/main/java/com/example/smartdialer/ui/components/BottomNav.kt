package com.example.smartdialer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp

@Composable
fun BottomNav(navController: NavHostController, modifier: Modifier = Modifier, openKeypad: () -> Unit) {

    // items: recents, dialer, contacts
    val items = listOf("recents" to "Recents", "dialer" to "Dial", "contacts" to "Contacts")
    val current = navController.currentBackStackEntry?.destination?.route ?: "recents"

    Box(modifier = modifier.wrapContentWidth().height(72.dp)) {
        Box(
            modifier = Modifier
                .wrapContentWidth()
                .height(64.dp)
                .padding(horizontal = 16.dp)
                .background(Color(0xFF0B0B0B), shape = RoundedCornerShape(40.dp))
                .border(width = 1.dp, color = Color(0x22FFFFFF), shape = RoundedCornerShape(40.dp))
                .padding(horizontal = 18.dp, vertical = 10.dp)
                .align(Alignment.Center)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(28.dp), verticalAlignment = Alignment.CenterVertically) {
                items.forEach { (route, label) ->
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                        if (route == "dialer") {
                            // center action opens keypad
                            openKeypad()
                        } else {
                            navController.navigate(route) {
                                launchSingleTop = true
                            }
                        }
                    }) {
                        // tiny dot for selection
                        val chosen = current == route
                        Box(modifier = Modifier.size(8.dp).background(if (chosen) Color.White else Color.Transparent, shape = RoundedCornerShape(8.dp)))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(text = label, color = if (chosen) Color.White else Color(0xAAFFFFFF), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

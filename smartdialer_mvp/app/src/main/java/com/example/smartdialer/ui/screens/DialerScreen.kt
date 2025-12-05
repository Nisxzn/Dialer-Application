package com.example.smartdialer.ui.screens

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.compose.ui.graphics.Color
import com.example.smartdialer.ui.components.DotNumber

@Composable
fun DialerScreen(onOpenContacts: () -> Unit, onOpenRecents: () -> Unit, openKeypad: () -> Unit, navController: NavHostController) {
    val ctx = LocalContext.current
    var number by remember { mutableStateOf("") }
    // if nav savedState has selectedNumber, retrieve
    val saved = navController.currentBackStackEntry?.savedStateHandle?.get<String>("selectedNumber")
    LaunchedEffect(saved) {
        if (saved != null) number = saved
        navController.currentBackStackEntry?.savedStateHandle?.set("selectedNumber", null)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        DotNumber(value = number, placeholder = "+91 123 456 7890")
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onOpenContacts) { Text("Contacts") }
            OutlinedButton(onClick = onOpenRecents) { Text("Recents") }
        }
        Spacer(modifier = Modifier.weight(1f))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = { if (number.isNotEmpty()) number = number.dropLast(1) }, modifier = Modifier.weight(1f)) { Text("Delete") }
            Spacer(modifier = Modifier.width(12.dp))
            Button(onClick = {
                if (number.isNotEmpty()) {
                    val it = Intent(Intent.ACTION_CALL, Uri.parse("tel:$number"))
                    ctx.startActivity(it)
                } else {
                    openKeypad()
                }
            }, modifier = Modifier.weight(2f), colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
                Text("Call")
            }
        }
    }
}

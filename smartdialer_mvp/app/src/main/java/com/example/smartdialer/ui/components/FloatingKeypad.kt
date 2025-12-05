package com.example.smartdialer.ui.components


import android.content.Intent
import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow

@Composable
fun FloatingKeypad(visible: Boolean, onDismiss: () -> Unit) {
    val ctx = LocalContext.current
    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } ,
        modifier = Modifier.fillMaxSize()
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0x99000000)).clickable { onDismiss() }) {
            // keypad container bottom
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(12.dp),
                shape = RoundedCornerShape(18.dp),
            ) {
                Column(modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFF000000))
                    .padding(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {

                    // dot-matrix number placeholder: you can show result using composable earlier
                    Text(text = "+91 123 456 7890", color = Color.White, fontSize = 26.sp)

                    Spacer(modifier = Modifier.height(10.dp))

                    // keypad grid
                    val rows = listOf(
                        listOf("1","2","3"),
                        listOf("4","5","6"),
                        listOf("7","8","9"),
                        listOf("*","0","#")
                    )
                    rows.forEach { row ->
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                            row.forEach { key ->
                                NothingKey(key) {
                                    // append to field if stored in parent — here we will call into app via context intent?
                                    // Simpler: show toast or no-op — prefer connecting via shared state; for demo kept local
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                        // small delete
                        Box(modifier = Modifier.weight(1f).height(56.dp).clip(RoundedCornerShape(28.dp)).background(Color(0xFF0B0B0B)).clickable {
                            // dismiss or handle delete
                        }, contentAlignment = Alignment.Center) {
                            Text("Delete", color = Color.White)
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        // big call button
                        Button(
                            onClick = {
                                // action handled by outer app — demo: open dialer
                                val i = Intent(Intent.ACTION_DIAL, Uri.parse("tel:"))
                                ctx.startActivity(i)
                            },
                            modifier = Modifier.weight(1.4f).height(56.dp).shadow(6.dp, CircleShape),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                        ) {
                            Text("Call", color = Color.Black)
                        }
                    }
                }
            }
        }
    }
}

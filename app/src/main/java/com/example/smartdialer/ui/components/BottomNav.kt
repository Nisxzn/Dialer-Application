package com.example.smartdialer.ui.components

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController

@Composable
fun BottomNav(
    nav: NavHostController,
    context: Context,
    onKeypadClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(top = 20.dp, bottom = 30.dp),

        horizontalArrangement = Arrangement.SpaceEvenly,
//        verticalAlignment = Alignment.CenterVertically
    ) {

        Text(
            text = "Recents",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.clickable { nav.navigate("recents") }
                .padding(start = 2.dp)
        )

        Text(
            text = "Dial",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.clickable { nav.navigate("dial") }
                .padding(start = 2.dp)
        )

        Text(
            text = "Contacts",
            fontSize = 20.sp,
            color = Color.White,
            modifier = Modifier.clickable { nav.navigate("contacts") }
                .padding(start = 2.dp)
        )
    }
}

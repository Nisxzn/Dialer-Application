package com.example.smartdialer.ui.screens

import android.content.Context
import android.provider.CallLog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons        // ✅ Added
//import androidx.compose.material.icons.filled.* // ✅ Added
import androidx.compose.material.icons.filled.CallMade
import androidx.compose.material.icons.filled.CallMissed
import androidx.compose.material.icons.filled.CallReceived

import androidx.compose.material3.Icon            // ✅ Added
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment               // ✅ Added
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color          // ✅ Added
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp                // ✅ Added
import com.example.smartdialer.CallLogItem
import com.example.smartdialer_mvp.MainActivity

@Composable
fun RecentsScreen(
    onSelect: (String) -> Unit,
    openKeypad: () -> Unit
){
    val ctx = androidx.compose.ui.platform.LocalContext.current
    var logs by remember { mutableStateOf(listOf<CallLogItem>()) }

    LaunchedEffect(Unit) {
        logs = loadCallLogs(ctx)
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(16.dp)
    ) {

        Text(
            "Recent",
            color = Color.White,
            fontSize = 42.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(top = 40.dp, bottom = 20.dp)
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn {
            items(logs) { e ->

                // ✅ Pick correct icon
                val (icon, Color) = when (e.type) {
                    "Incoming" -> Icons.Filled.CallReceived to Color.White
                    "Outgoing" -> Icons.Filled.CallMade to Color.White
                    else -> Icons.Filled.CallMissed to Color.Red
                }

                Row(
                    Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(e.number) }
                        .padding(vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    // CALL TYPE ICON
                    Icon(
                        icon,
                        contentDescription = null,
                        tint = Color,
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(Modifier.width(12.dp))

                    Column(Modifier.weight(1f)) {

                        Text(
                            e.name ?: e.number,
                            color = Color,
                            fontSize = 18.sp
                        )

                        Text(
                            e.number,
                            color = Color,
                            fontSize = 16.sp
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            e.time,
                            color = Color,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

fun loadCallLogs(context: Context): List<CallLogItem> {
    val list = mutableListOf<CallLogItem>()
    val resolver = context.contentResolver
    val cursor = resolver.query(
        CallLog.Calls.CONTENT_URI,
        null, null, null,
        CallLog.Calls.DATE + " DESC"
    )

    cursor?.use {
        val nIdx = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
        val pIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
        val tIdx = it.getColumnIndex(CallLog.Calls.TYPE)
        val dIdx = it.getColumnIndex(CallLog.Calls.DATE)

        while (it.moveToNext()) {
            val name = it.getString(nIdx)
            val number = it.getString(pIdx) ?: continue
            val typeCode = it.getInt(tIdx)
            val timeMillis = it.getLong(dIdx)

            val type = when (typeCode) {
                CallLog.Calls.INCOMING_TYPE -> "Incoming"
                CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                CallLog.Calls.MISSED_TYPE -> "Missed"
                else -> "Other"
            }

            val time = java.text.SimpleDateFormat(
                "dd MMM, hh:mm a"
            ).format(java.util.Date(timeMillis))

            list.add(CallLogItem(name, number, type, time))
        }
    }
    return list
}

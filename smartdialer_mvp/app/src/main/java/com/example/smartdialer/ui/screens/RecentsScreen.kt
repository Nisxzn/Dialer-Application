package com.example.smartdialer.ui.screens

import android.content.ContentResolver
import android.content.Context
import android.provider.CallLog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.smartdialer.CallLogItem   // ✅ correct model import

@Composable
fun RecentsScreen(
    onSelect: (String) -> Unit,
    openKeypad: () -> Unit
) {
    val ctx = LocalContext.current

    var logs by remember { mutableStateOf(emptyList<CallLogItem>()) }

    // Load call logs
    LaunchedEffect(Unit) {
        logs = loadCallLogs(ctx)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        Text(
            text = "Recent Calls",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(logs) { entry ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(entry.number) }
                        .padding(12.dp)
                ) {
                    Text(entry.name ?: entry.number)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("${entry.type} • ${entry.time}")
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = openKeypad) {
            Text("Open Dialpad")
        }
    }
}

fun loadCallLogs(context: Context): List<CallLogItem> {
    val list = mutableListOf<CallLogItem>()
    val resolver: ContentResolver = context.contentResolver

    val cursor = resolver.query(
        CallLog.Calls.CONTENT_URI,
        null,
        null,
        null,
        CallLog.Calls.DATE + " DESC"
    )

    cursor?.use {
        val nameIdx = it.getColumnIndex(CallLog.Calls.CACHED_NAME)
        val numIdx = it.getColumnIndex(CallLog.Calls.NUMBER)
        val typeIdx = it.getColumnIndex(CallLog.Calls.TYPE)
        val dateIdx = it.getColumnIndex(CallLog.Calls.DATE)

        while (it.moveToNext()) {

            val name = it.getString(nameIdx)
            val number = it.getString(numIdx) ?: continue
            val typeCode = it.getInt(typeIdx)
            val timeMillis = it.getLong(dateIdx)

            val type = when (typeCode) {
                CallLog.Calls.INCOMING_TYPE -> "Incoming"
                CallLog.Calls.OUTGOING_TYPE -> "Outgoing"
                CallLog.Calls.MISSED_TYPE -> "Missed"
                CallLog.Calls.REJECTED_TYPE -> "Rejected"
                else -> "Other"
            }

            val time = java.text.SimpleDateFormat("dd MMM, hh:mm a")
                .format(java.util.Date(timeMillis))

            list.add(CallLogItem(name, number, type, time))
        }
    }

    return list
}

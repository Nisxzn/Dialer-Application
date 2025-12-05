package com.example.smartdialer.ui.screens

import android.content.ContentResolver
import android.content.Context
import android.provider.ContactsContract
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.smartdialer.Contact   // ✅ Correct import for your Contact model

@Composable
fun ContactsScreen(
    onSelect: (String) -> Unit,
    context: Context
) {
    var contacts by remember { mutableStateOf(emptyList<Contact>()) }

    // Load contacts once
    LaunchedEffect(Unit) {
        contacts = loadContacts(context)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp)
    ) {

        SearchBar()

        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(contacts) { c ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onSelect(c.number) }
                        .padding(12.dp)
                ) {
                    Text(text = c.name)
                    Spacer(modifier = Modifier.weight(1f))
                    Text(text = c.number)
                }
            }
        }
    }
}

@Composable
fun SearchBar() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp))
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        Text(
            text = "Search",
            color = Color(0x55FFFFFF)
        )
    }
}

fun loadContacts(context: Context): List<Contact> {
    val list = mutableListOf<Contact>()
    val resolver: ContentResolver = context.contentResolver

    val cursor = resolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null,
        null,
        null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )

    cursor?.use {
        val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)

        while (it.moveToNext()) {
            val name = it.getString(nameIdx) ?: continue
            val number = it.getString(numIdx) ?: continue
            list.add(Contact(name, number))   // ✅ uses your model correctly
        }
    }

    return list
}

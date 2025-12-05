package com.example.smartdialer.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.smartdialer.Contact
import com.example.smartdialer.loadAllContacts
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactsScreen(onSelect: (String) -> Unit) {

    val ctx = LocalContext.current
    val scope = rememberCoroutineScope()

    var contacts by remember { mutableStateOf(listOf<Contact>()) }
    var searchActive by remember { mutableStateOf(false) }
    var searchQuery by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        contacts = loadAllContacts(ctx)
    }

    // SEARCH FILTER
    val filtered = if (searchActive && searchQuery.isNotBlank()) {
        contacts.filter {
            it.name.contains(searchQuery, true) || it.number.contains(searchQuery)
        }
    } else contacts

    // A-Z GROUPING
    val grouped = filtered
        .groupBy { it.name.firstOrNull()?.uppercase() ?: "#" }
        .toSortedMap()

    val listState = rememberLazyListState()
    val sectionPositions = remember { mutableMapOf<String, Int>() }

    // MAIN LAYOUT
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(horizontal = 20.dp)
    ) {

        // ---------------------------------------------------------
        // HEADER (Title + Icons row + Search bar)
        // ---------------------------------------------------------
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 35.dp)
        ) {

            // TITLE ROW (Text left, icons right)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Contacts",
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(top = 15.dp)

                )

                Row {

                    // Search icon
                    IconButton(onClick = {
                        searchActive = !searchActive
                        if (!searchActive) searchQuery = ""
                    }) {
                        Icon(
                            Icons.Default.Search,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(80.dp)
                                .padding(top = 15.dp)
                        )
                    }

                    // More options icon
                    IconButton(onClick = { }) {
                        Icon(
                            Icons.Default.MoreVert,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(80.dp)
                                .padding(top = 15.dp)

                        )
                    }
                }
            }

            // SEARCH TEXT FIELD (Nothing OS style)
            if (searchActive) {
                Spacer(Modifier.height(14.dp))

                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    singleLine = true,
                    placeholder = { Text("Search contacts…", color = Color.Gray) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp)),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFF151515),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        cursorColor = Color.White,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
            }
        }

        // ---------------------------------------------------------
        // CONTACT LIST (LazyColumn)
        // ---------------------------------------------------------
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    top = if (searchActive) 180.dp else 120.dp,
                    bottom = 90.dp
                )
        ) {

            var indexCounter = 0

            grouped.forEach { (letter, list) ->

                sectionPositions[letter] = indexCounter

                // LETTER HEADER
                item {
                    Text(
                        text = letter,
                        color = Color(0xFFB5B5B5),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(
                            start = 4.dp,
                            top = 18.dp,
                            bottom = 12.dp
                        )
                    )
                }
                indexCounter++

                // CONTACT ROWS
                itemsIndexed(list) { _, c ->
                    ContactRow(c) { onSelect(c.number) }
                    indexCounter++
                }
            }
        }

        // ---------------------------------------------------------
        // RIGHT-SIDE ALPHABET NAVIGATION
        // ---------------------------------------------------------
        Column(
            modifier = Modifier
                .align(Alignment.CenterEnd)
                .padding(end = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            grouped.keys.forEach { letter ->
                Text(
                    text = letter,
                    color = Color.Gray,
                    fontSize = 13.sp,
                    modifier = Modifier
                        .padding(start = 4.dp, end = 4.dp, top = 2.dp)

                        .clickable {
                            sectionPositions[letter]?.let { pos ->
                                scope.launch { listState.scrollToItem(pos) }
                            }
                        }
                )
            }
        }
    }
}

// ---------------------------------------------------------
// SINGLE CONTACT ROW (Nothing OS Style)
// ---------------------------------------------------------
@Composable
private fun ContactRow(c: Contact, onClick: () -> Unit) {

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 14.dp)
            .clickable { onClick() },
        verticalAlignment = Alignment.CenterVertically
    ) {

        // Avatar
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(CircleShape)
                .background(Color(0xFFE930FF)),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = c.name.first().uppercase(),
                color = Color.White,
                fontSize = 22.sp
            )
        }

        Spacer(Modifier.width(20.dp))

        Column {
            Text(
                text = c.name,
                color = Color.White,
                fontSize = 18.sp
            )
            Text(
                text = c.number,
                color = Color(0xFFD0D0D0),
                fontSize = 16.sp
            )
        }
    }
}

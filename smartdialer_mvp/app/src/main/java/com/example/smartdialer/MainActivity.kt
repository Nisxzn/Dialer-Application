package com.example.smartdialer_mvp

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.telecom.TelecomManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

// ----------------- Models -----------------
data class Contact(val name: String, val number: String)
data class CallLogItem(val name: String?, val number: String, val type: String, val time: String)

// ----------------- MainActivity -----------------
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Optionally prompt user to set default dialer (uncomment if you want)
        // maybeRequestDefaultDialer()

        setContent {
            NothingBlackTheme {
                val navController = rememberNavController()
                // central shared number state so keypad and dialer share it
                val numberState = remember { mutableStateOf("") }
                // show/hide floating keypad
                val showKeypad = remember { mutableStateOf(false) }

                // ask needed runtime permissions together
                val permissionsLauncher = rememberLauncherForActivityResult(
                    ActivityResultContracts.RequestMultiplePermissions()
                ) { perms ->
                    // perms map returned, we don't force-action here; UI will request when needed
                }
                LaunchedEffect(Unit) {
                    permissionsLauncher.launch(
                        arrayOf(
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG
                        )
                    )
                }

                Scaffold(
                    containerColor = Color.Black,
                    content = { padding ->
                        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
                            NavHost(navController = navController, startDestination = "dial") {
                                composable("dial") {
                                    DialerScreen(
                                        numberState = numberState,
                                        onOpenContacts = { navController.navigate("contacts") },
                                        onOpenRecents = { navController.navigate("recents") },
                                        openKeypad = { showKeypad.value = true }
                                    )
                                }
                                composable("contacts") {
                                    ContactsScreen(
                                        onSelect = { selected ->
                                            numberState.value = selected
                                            navController.navigate("dial")
                                        }
                                    )
                                }
                                composable("recents") {
                                    RecentsScreen(
                                        onSelect = { selected ->
                                            numberState.value = selected
                                            navController.navigate("dial")
                                        },
                                        openKeypad = { showKeypad.value = true }
                                    )
                                }
                            }

                            // Floating Keypad - pass numberState so keys update the number
                            FloatingKeypad(
                                visible = showKeypad.value,
                                onDismiss = { showKeypad.value = false },
                                numberState = numberState
                            )

                            // Floating bottom nav pill
                            FloatingBottomNav(
                                navController = navController,
                                modifier = Modifier.align(Alignment.BottomCenter).padding(bottom = 18.dp),
                                onDialClick = { showKeypad.value = true }
                            )
                        }
                    }
                )
            }
        }
    }

    private fun maybeRequestDefaultDialer() {
        val telecom = getSystemService(TELECOM_SERVICE) as TelecomManager
        val pkg = packageName
        if (telecom.defaultDialerPackage != pkg) {
            startActivity(
                Intent(TelecomManager.ACTION_CHANGE_DEFAULT_DIALER).apply {
                    putExtra(TelecomManager.EXTRA_CHANGE_DEFAULT_DIALER_PACKAGE_NAME, pkg)
                }
            )
        }
    }
}

// ----------------- THEME -----------------
@Composable
fun NothingBlackTheme(content: @Composable () -> Unit) {
    val colors = darkColorScheme(
        background = Color(0xFF000000),
        surface = Color(0xFF000000),
        onSurface = Color.White,
        primary = Color.White
    )
    MaterialTheme(
        colorScheme = colors,
        typography = Typography(
            titleLarge = androidx.compose.ui.text.TextStyle(fontSize = 22.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.SansSerif),
            bodyLarge = androidx.compose.ui.text.TextStyle(fontSize = 16.sp, fontFamily = FontFamily.SansSerif)
        ),
        content = content
    )
}

// ----------------- Dialer Screen -----------------
@Composable
fun DialerScreen(
    numberState: MutableState<String>,
    onOpenContacts: () -> Unit,
    onOpenRecents: () -> Unit,
    openKeypad: () -> Unit
) {
    val number by numberState
    val ctx = LocalContext.current
    var t9Results by remember { mutableStateOf<List<Contact>>(emptyList()) }
    var allContacts by remember { mutableStateOf<List<Contact>>(emptyList()) }

    // load contacts once for T9 suggestions asynchronously
    LaunchedEffect(Unit) {
        allContacts = loadAllContacts(ctx)
    }

    // compute T9 results whenever number changes (digits only)
    LaunchedEffect(number) {
        val digits = number.filter { it.isDigit() }
        t9Results = if (digits.isNotEmpty()) {
            allContacts.filter { T9Utils.matchesT9(digits, it.name) }
        } else emptyList()
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(16.dp)) {
        // display
        Text(
            text = if (number.isNotEmpty()) number else "Enter number or select contact",
            color = Color.White,
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.height(12.dp))
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            OutlinedButton(onClick = onOpenContacts, modifier = Modifier.weight(1f)) { Text("Contacts") }
            OutlinedButton(onClick = onOpenRecents, modifier = Modifier.weight(1f)) { Text("Recents") }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // T9 suggestions
        if (t9Results.isNotEmpty()) {
            LazyColumn(modifier = Modifier.heightIn(max = 160.dp)) {
                items(t9Results) { c ->
                    Row(modifier = Modifier
                        .fillMaxWidth()
                        .clickable { numberState.value = c.number }
                        .padding(12.dp)) {
                        Text(c.name, color = Color.White, modifier = Modifier.weight(1f))
                        Text(c.number, color = Color.Gray)
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }

        // keypad rows (preview). main keypad is floating but we still allow basic buttons here
        val rows = listOf(
            listOf("1","2","3"),
            listOf("4","5","6"),
            listOf("7","8","9"),
            listOf("*","0","#")
        )

        rows.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                row.forEach { k ->
                    SmallKey(k) { numberState.value = numberState.value + k }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            OutlinedButton(onClick = {
                if (numberState.value.isNotEmpty()) numberState.value = numberState.value.dropLast(1)
            }) {
                Text("Delete")
            }

            Button(onClick = {
                // direct call using ACTION_CALL requires CALL_PHONE permission
                if (numberState.value.isNotBlank()) {
                    val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${numberState.value}"))
                    ctx.startActivity(callIntent)
                } else {
                    // if no number, open keypad
                    openKeypad()
                }
            }) {
                Text("Call")
            }
        }
    }
}

// simple small key component
@Composable
fun SmallKey(k: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(68.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFF0B0B0B))
            .border(1.dp, Color(0x22FFFFFF), shape = RoundedCornerShape(18.dp))
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(text = k, color = Color.White, fontSize = 20.sp)
    }
}

// ----------------- Contacts Screen -----------------
@Composable
fun ContactsScreen(onSelect: (String) -> Unit) {
    val ctx = LocalContext.current
    var contacts by remember { mutableStateOf<List<Contact>>(emptyList()) }
    var query by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        contacts = loadAllContacts(ctx)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(12.dp)) {
        // Search-like UI
        Box(modifier = Modifier.fillMaxWidth().height(48.dp).clip(RoundedCornerShape(12.dp)).background(Color(0xFF0B0B0B)).padding(12.dp)) {
            Text(text = "Search contacts", color = Color(0x66FFFFFF))
        }
        Spacer(modifier = Modifier.height(12.dp))

        LazyColumn {
            items(contacts.filter { it.name.contains(query, ignoreCase = true) || query.isBlank() }) { c ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(c.number) }
                    .padding(vertical = 10.dp)) {
                    Text(c.name, color = Color.White, modifier = Modifier.weight(1f))
                    Text(c.number, color = Color.Gray)
                }
            }
        }
    }
}

// ----------------- Recents Screen -----------------
@Composable
fun RecentsScreen(onSelect: (String) -> Unit, openKeypad: () -> Unit) {
    val ctx = LocalContext.current
    var logs by remember { mutableStateOf<List<CallLogItem>>(emptyList()) }

    LaunchedEffect(Unit) {
        logs = loadCallLogs(ctx)
    }

    Column(modifier = Modifier.fillMaxSize().background(Color.Black).padding(12.dp)) {
        Text("Recent calls", color = Color.White, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(12.dp))
        LazyColumn {
            items(logs) { e ->
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(e.number) }
                    .padding(vertical = 10.dp)) {
                    Text(e.name ?: e.number, color = Color.White, modifier = Modifier.weight(1f))
                    Text("${e.type} • ${e.time}", color = Color.Gray)
                }
            }
        }
        Spacer(modifier = Modifier.height(12.dp))
        OutlinedButton(onClick = openKeypad) {
            Text("Open Dialpad")
        }
    }
}

// ----------------- Floating Keypad -----------------
@Composable
fun FloatingKeypad(visible: Boolean, onDismiss: () -> Unit, numberState: MutableState<String>) {
    val ctx = LocalContext.current

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically { it } + fadeIn(),
        exit = slideOutVertically { it } + fadeOut()
    ) {
        Box(modifier = Modifier.fillMaxSize().background(Color(0x80000000)).clickable { onDismiss() }) {
            // keypad container
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .clip(RoundedCornerShape(18.dp))
                    .background(Color(0xFF000000))
                    .padding(16.dp)
            ) {
                // display typed number
                Text(numberState.value.ifBlank { "Enter number" }, color = Color.White, fontSize = 20.sp)
                Spacer(modifier = Modifier.height(12.dp))

                val rows = listOf(
                    listOf("1","2","3"),
                    listOf("4","5","6"),
                    listOf("7","8","9"),
                    listOf("*","0","#")
                )

                rows.forEach { row ->
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                        row.forEach { k ->
                            Box(
                                modifier = Modifier
                                    .size(86.dp)
                                    .clip(RoundedCornerShape(26.dp))
                                    .background(Color(0xFF0B0B0B))
                                    .border(1.dp, Color(0x22FFFFFF), RoundedCornerShape(26.dp))
                                    .clickable {
                                        numberState.value += k
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(k, color = Color.White, fontSize = 22.sp)
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                Spacer(modifier = Modifier.height(8.dp))

                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    OutlinedButton(onClick = { if (numberState.value.isNotEmpty()) numberState.value = numberState.value.dropLast(1) }, modifier = Modifier.weight(1f)) {
                        Text("Delete")
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(onClick = {
                        if (numberState.value.isNotBlank()) {
                            val callIntent = Intent(Intent.ACTION_CALL, Uri.parse("tel:${numberState.value}"))
                            ctx.startActivity(callIntent)
                        }
                    }, modifier = Modifier.weight(1.6f)) {
                        Text("Call")
                    }
                }
            }
        }
    }
}

// ----------------- Floating bottom nav -----------------
@Composable
fun FloatingBottomNav(navController: NavHostController, modifier: Modifier = Modifier, onDialClick: () -> Unit) {
    val items = listOf("recents" to "Recents", "dial" to "Dial", "contacts" to "Contacts")
    val backStack = navController.currentBackStackEntry
    val current = backStack?.destination?.route ?: "recents"

    Box(modifier = modifier.wrapContentWidth().height(72.dp)) {
        Box(modifier = Modifier
            .wrapContentWidth()
            .height(64.dp)
            .padding(horizontal = 16.dp)
            .background(Color(0xFF0B0B0B), shape = RoundedCornerShape(40.dp))
            .border(1.dp, Color(0x22FFFFFF), shape = RoundedCornerShape(40.dp))
            .padding(horizontal = 18.dp, vertical = 10.dp)
            .align(Alignment.Center)
        ) {
            Row(horizontalArrangement = Arrangement.spacedBy(28.dp), verticalAlignment = Alignment.CenterVertically) {
                items.forEach { (route, label) ->
                    val selected = route == current
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable {
                        if (route == "dial") onDialClick() else navController.navigate(route) { launchSingleTop = true }
                    }) {
                        Box(modifier = Modifier.size(8.dp).clip(CircleShape).background(if (selected) Color.White else Color.Transparent))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(label, color = if (selected) Color.White else Color(0xAAFFFFFF), fontSize = 12.sp)
                    }
                }
            }
        }
    }
}

// ----------------- Helpers -----------------
suspend fun queryContactsAsync(context: Context): List<Contact> = withContext(Dispatchers.IO) { loadAllContacts(context) }

fun loadAllContacts(context: Context): List<Contact> {
    val list = mutableListOf<Contact>()
    val resolver: ContentResolver = context.contentResolver
    val cursor = resolver.query(
        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
        null, null, null,
        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
    )
    cursor?.use {
        val nameIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
        val numIdx = it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
        while (it.moveToNext()) {
            val name = it.getString(nameIdx) ?: continue
            val number = it.getString(numIdx) ?: continue
            list.add(Contact(name, number))
        }
    }
    return list
}

fun loadCallLogs(context: Context): List<CallLogItem> {
    val list = mutableListOf<CallLogItem>()
    val resolver = context.contentResolver
    val cursor = resolver.query(CallLog.Calls.CONTENT_URI, null, null, null, CallLog.Calls.DATE + " DESC")
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
            val time = SimpleDateFormat("dd MMM, hh:mm a", Locale.getDefault()).format(Date(timeMillis))
            list.add(CallLogItem(name, number, type, time))
        }
    }
    return list
}

// ----------------- T9 UTILS -----------------


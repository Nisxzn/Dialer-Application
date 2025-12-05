package com.example.smartdialer_mvp

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.telecom.TelecomManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.view.WindowCompat
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.smartdialer.ui.NothingTheme
import com.example.smartdialer.ui.components.BottomNav
import com.example.smartdialer.ui.components.FloatingKeypad
import com.example.smartdialer.ui.screens.ContactsScreen
import com.example.smartdialer.ui.screens.DialerScreen
import com.example.smartdialer.ui.screens.RecentsScreen
import androidx.compose.material3.Scaffold

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        WindowCompat.setDecorFitsSystemWindows(window, false)

        setContent {

            NothingTheme {

                val nav = rememberNavController()
                val context = LocalContext.current

                var showKeypad by remember { mutableStateOf(false) }

                val permissionLauncher =
                    rememberLauncherForActivityResult(
                        ActivityResultContracts.RequestMultiplePermissions()
                    ) {}

                LaunchedEffect(Unit) {
                    permissionLauncher.launch(
                        arrayOf(
                            Manifest.permission.CALL_PHONE,
                            Manifest.permission.READ_CONTACTS,
                            Manifest.permission.READ_CALL_LOG
                        )
                    )
                }

                // *************** FIX: USE SCAFFOLD *****************
                Scaffold(
                    bottomBar = {
                        BottomNav(
                            nav = nav,
                            context = context,
                            onKeypadClick = { showKeypad = true }
                        )
                    },
                    containerColor = androidx.compose.ui.graphics.Color.Transparent
                ) { padding ->

                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {

                        NavHost(
                            navController = nav,
                            startDestination = "dial",
                            modifier = Modifier
                                .fillMaxSize()
                        ) {

                            composable("dial") {
                                DialerScreen(
//                                    context = context,
                                    navToContacts = { nav.navigate("contacts") },
                                    navToRecents = { nav.navigate("recents") },
                                    openKeypad = { showKeypad = true }
                                )
                            }

                            composable("contacts") {
                                ContactsScreen(
                                    onSelect = { number ->
                                        nav.navigate("dial") { launchSingleTop = true }
                                        nav.previousBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("selectedNumber", number)
                                    }
                                )
                            }

                            composable("recents") {
                                RecentsScreen(
                                    onSelect = { number ->
                                        nav.navigate("dial") { launchSingleTop = true }
                                        nav.previousBackStackEntry
                                            ?.savedStateHandle
                                            ?.set("selectedNumber", number)
                                    },
                                    openKeypad = { showKeypad = true }
                                )
                            }
                        }

                        // Floating Keypad
                        FloatingKeypad(
                            visible = showKeypad,
                            onDismiss = { showKeypad = false },
                            content = {}
                        )
                    }
                }
            }
        }
    }

    fun placeCall(number: String) {
        if (number.isBlank()) return

        val i = Intent(Intent.ACTION_CALL).apply {
            data = Uri.parse("tel:$number")
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        startActivity(i)
    }
}

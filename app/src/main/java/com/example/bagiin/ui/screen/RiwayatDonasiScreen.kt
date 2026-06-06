package com.example.bagiin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.outlined.AddCircleOutline
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.saveable.rememberSaveableStateHolder
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bagiin.model.HistoryItem
import com.example.bagiin.viewmodel.HistoryViewModel

@Composable
fun RiwayatDonasiScreen(
    navController: NavController,
    viewModel: HistoryViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var aktivitasInput by rememberSaveable { mutableStateOf("") }
    val listState = rememberLazyListState()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("dashboard") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Outlined.Home, contentDescription = "Home")
                    },
                    label = { Text("Home") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("upload_donasi") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Outlined.AddCircleOutline, contentDescription = "Upload")
                    },
                    label = { Text("Upload") }
                )

                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = {
                        Icon(Icons.Default.History, contentDescription = "Riwayat")
                    },
                    label = { Text("Riwayat") }
                )

                NavigationBarItem(
                    selected = false,
                    onClick = {
                        navController.navigate("profile") {
                            launchSingleTop = true
                        }
                    },
                    icon = {
                        Icon(Icons.Outlined.Person, contentDescription = "Profile")
                    },
                    label = { Text("Profile") }
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Riwayat Donasi",
                        style = MaterialTheme.typography.headlineSmall
                    )

                    Text(
                        text = "Aktivitas donasi barang layak pakai",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }

                IconButton(
                    onClick = {
                        viewModel.loadHistory()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh"
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = aktivitasInput,
                onValueChange = {
                    aktivitasInput = it
                },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("Catat aktivitas riwayat")
                },
                placeholder = {
                    Text("Contoh: Mengunggah barang donasi")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = {
                    viewModel.addHistory(aktivitasInput)
                    aktivitasInput = ""
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isLoading
            ) {
                Text("Tambah Riwayat")
            }

            Spacer(modifier = Modifier.height(12.dp))

            uiState.errorMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            uiState.successMessage?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                uiState.historyList.isEmpty() -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Belum ada riwayat donasi")
                    }
                }

                else -> {
                    LazyColumn(
                        state = listState,
                        verticalArrangement = Arrangement.spacedBy(10.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(
                            items = uiState.historyList,
                            key = { item -> item.id }
                        ) { item ->
                            HistoryRowItem(item = item)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryRowItem(
    item: HistoryItem
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = item.aktivitas,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Tanggal: ${formatTanggal(item.tanggal)}",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

fun formatTanggal(tanggal: String?): String {
    if (tanggal.isNullOrBlank()) return "-"

    return tanggal
        .replace("T", " ")
        .substringBefore(".")
}
package com.example.bagiin.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.bagiin.viewmodel.JadwalPenyerahanViewModel

// Luminous Giving Colors
private val LGPrimary = Color(0xFF004AC6)
private val LGPrimaryContainer = Color(0xFF2563EB)
private val LGOnPrimary = Color(0xFFFFFFFF)
private val LGBackground = Color(0xFFF8F9FF)
private val LGSurfaceLowest = Color(0xFFFFFFFF)
private val LGSurfaceContainer = Color(0xFFE5EEFF)
private val LGSurfaceContainerLow = Color(0xFFEFF4FF)
private val LGOnBackground = Color(0xFF0B1C30)
private val LGOnSurfaceVariant = Color(0xFF434655)
private val LGOutline = Color(0xFF737686)
private val LGOutlineVariant = Color(0xFFC3C6D7)
private val LGSuccessGreen = Color(0xFF16A34A)
private val LGSuccessGreenBg = Color(0xFFDCFCE7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalPenyerahanScreen(navController: NavController, itemTitle: String, viewModel: JadwalPenyerahanViewModel = viewModel()) {
    // Text Input States
    val additionalInstructions = viewModel.additionalInstructions
    val selectedDateText = viewModel.selectedDateText
    val selectedTimeText = viewModel.selectedTimeText
    val showSuccessDialog = viewModel.showSuccessDialog

    var meetingLocation by remember { mutableStateOf("") }

    // State for controlling date/time dialog visibility
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // States for the Pickers themselves
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(
        initialHour = 14,
        initialMinute = 0,
        is24Hour = false
    )

    // ==================== SUCCESS DIALOG ====================
    if (showSuccessDialog) {
        Dialog(
            onDismissRequest = { },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = LGSurfaceLowest
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    // Success Icon
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .clip(CircleShape)
                            .background(LGSuccessGreenBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = LGSuccessGreen,
                            modifier = Modifier.size(52.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Jadwal Berhasil\nDibuat!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = LGOnBackground,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp,
                        letterSpacing = (-0.01).sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "Jadwal penyerahan barang telah\ndikonfirmasi. Pastikan Anda datang tepat\nwaktu di lokasi yang disepakati.",
                        fontSize = 15.sp,
                        color = LGOnSurfaceVariant,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // View History Button
                    Button(
                        onClick = {
                            viewModel.dismissDialog()
                            navController.navigate("riwayat_donasi") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LGPrimaryContainer)
                    ) {
                        Icon(
                            Icons.Outlined.History,
                            contentDescription = null,
                            tint = LGOnPrimary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Lihat Riwayat",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LGOnPrimary
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Close Button
                    TextButton(
                        onClick = {
                            viewModel.dismissDialog()
                            navController.navigate("dashboard") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            "Tutup",
                            color = LGPrimaryContainer,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }

    // ==================== DATE PICKER DIALOG ====================
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.dismissDialog()
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                            viewModel.updateDate(
                                formatter.format(Date(millis))
                            )
                        }
                    }
                ) {
                    Text("OK", color = LGPrimaryContainer)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("Batal", color = LGOnBackground)
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    // ==================== TIME PICKER DIALOG ====================
    if (showTimePicker) {
        AlertDialog(
            onDismissRequest = { showTimePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        showTimePicker = false
                        val hour = timePickerState.hour
                        val minute = timePickerState.minute
                        val isAm = hour < 12
                        val amPm = if (isAm) "AM" else "PM"
                        val formattedHour = if (hour % 12 == 0) 12 else hour % 12
                        viewModel.updateTime(
                            String.format(
                                Locale.getDefault(),
                                "%d:%02d %s",
                                formattedHour,
                                minute,
                                amPm
                            )
                        )
                    }
                ) {
                    Text("OK", color = LGPrimaryContainer)
                }
            },
            dismissButton = {
                TextButton(onClick = { showTimePicker = false }) {
                    Text("Batal", color = LGOnBackground)
                }
            },
            text = {
                Box(
                    modifier = Modifier.fillMaxWidth(),
                    contentAlignment = Alignment.Center
                ) {
                    TimePicker(state = timePickerState)
                }
            }
        )
    }

    // ==================== MAIN SCREEN ====================
    Scaffold(
        containerColor = LGBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Jadwal Penyerahan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LGOnBackground
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = LGOnBackground
                        )
                    }
                },
                actions = {
                    IconButton(onClick = { /* notification */ }) {
                        Icon(
                            Icons.Outlined.Notifications,
                            contentDescription = "Notifications",
                            tint = LGOnBackground
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = LGSurfaceLowest
                )
            )
        },
        bottomBar = {
            Surface(
                color = LGSurfaceLowest,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Cancel button
                    OutlinedButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .weight(1f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, LGOutlineVariant)
                    ) {
                        Text(
                            "Batal",
                            color = LGOnBackground,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    // Confirm button
                    Button(
                        onClick = { viewModel.confirmSchedule() },
                        modifier = Modifier
                            .weight(1.5f)
                            .height(52.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = LGPrimaryContainer),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = LGOnPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            "Konfirmasi\nJadwal",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LGOnPrimary,
                            textAlign = TextAlign.Center,
                            lineHeight = 16.sp
                        )
                    }
                }
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ==================== ITEM INFO CARD ====================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LGSurfaceLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Item icon
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(LGSurfaceContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.Inventory,
                            contentDescription = null,
                            tint = LGPrimaryContainer,
                            modifier = Modifier.size(28.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "SCHEDULING FOR",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = LGOnSurfaceVariant,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = itemTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = LGOnBackground,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Outlined.Person,
                                contentDescription = null,
                                tint = LGSuccessGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Donated by Alex Chen",
                                fontSize = 12.sp,
                                color = LGSuccessGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ==================== WHEN SECTION ====================
            Text(
                text = "Kapan Anda ingin bertemu?",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = LGOnBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Date Picker field
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                shape = RoundedCornerShape(12.dp),
                color = LGSurfaceContainerLow,
                border = BorderStroke(1.dp, LGOutlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Default.DateRange,
                        contentDescription = null,
                        tint = LGOnSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = selectedDateText,
                        fontSize = 14.sp,
                        color = LGOnBackground,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Time Picker field
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker = true },
                shape = RoundedCornerShape(12.dp),
                color = LGSurfaceContainerLow,
                border = BorderStroke(1.dp, LGOutlineVariant.copy(alpha = 0.5f))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedTimeText,
                        fontSize = 14.sp,
                        color = LGOnBackground,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = LGOnSurfaceVariant,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ==================== ADDITIONAL INSTRUCTIONS ====================
            Text(
                text = "Instruksi Tambahan (Opsional)",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = LGOnBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = additionalInstructions,
                onValueChange = {
                    viewModel.updateInstructions(it)
                },
                placeholder = {
                    Text(
                        "e.g., 'Saya akan memakai jaket merah' atau 'Hubungi saya saat tiba'",
                        color = LGOutline,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = LGOutlineVariant.copy(alpha = 0.5f),
                    focusedBorderColor = LGPrimaryContainer,
                    cursorColor = LGPrimaryContainer,
                    unfocusedContainerColor = LGSurfaceContainerLow,
                    focusedContainerColor = LGSurfaceLowest
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    color = LGOnBackground
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
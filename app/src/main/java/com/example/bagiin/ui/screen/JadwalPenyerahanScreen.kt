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
import androidx.compose.material.icons.filled.*
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
import com.example.bagiin.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JadwalPenyerahanScreen(navController: NavController, itemTitle: String) {
    // 1. Text Input States
    var meetingLocation by remember { mutableStateOf("") }
    var additionalInstructions by remember { mutableStateOf("") }

    // 2. Dialog State
    var showSuccessDialog by remember { mutableStateOf(false) }

    // Assuming you have these defined in your theme.
    val localGrey = Color(0xFFF5F6F8)
    val localBorderGrey = Color(0xFFEEEEEE)

    // 3. State for displaying the selected Date/Time in the UI
    var selectedDateText by remember { mutableStateOf("11/15/2023") }
    var selectedTimeText by remember { mutableStateOf("2:00 PM") }

    // 4. State for controlling date/time dialog visibility
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }

    // 5. States for the Pickers themselves
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(
        initialHour = 14, // 2 PM
        initialMinute = 0,
        is24Hour = false
    )

    // --- FULL SCREEN SUCCESS DIALOG ---
    if (showSuccessDialog) {
        Dialog(
            onDismissRequest = { /* Prevent dismiss to force user to click a button */ },
            properties = DialogProperties(
                usePlatformDefaultWidth = false // This allows the dialog to be full screen
            )
        ) {
            Surface(
                modifier = Modifier.fillMaxSize(),
                color = Color.White
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
                            .background(BagiinGreen.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = "Success",
                            tint = BagiinGreen,
                            modifier = Modifier.size(52.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Schedule Successfully\nCreated!",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = BagiinDarkText,
                        textAlign = TextAlign.Center,
                        lineHeight = 32.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "The item handover schedule has been\nconfirmed. Please ensure you arrive on\ntime at the agreed location.",
                        fontSize = 15.sp,
                        color = BagiinGreyText,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp
                    )

                    Spacer(modifier = Modifier.height(40.dp))

                    // View History Button
                    Button(
                        onClick = {
                            showSuccessDialog = false
                            navController.navigate("riwayat_donasi") {
                                // Pop everything up to the start destination to prevent going back to the schedule screen
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = BagiinGreen)
                    ) {
                        Icon(
                            Icons.Default.List, // Standard receipt/list icon replacement
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("View History", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Close Button
                    TextButton(
                        onClick = {
                            showSuccessDialog = false
                            navController.navigate("dashboard") {
                                popUpTo(navController.graph.startDestinationId) {
                                    inclusive = false
                                }
                                launchSingleTop = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Close", color = BagiinGreen, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFFAFAFA))
    ) {
        // --- TOP BAR ---
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, start = 8.dp, end = 16.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = BagiinDarkText
                )
            }
            Text(
                text = "Schedule Handover",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = BagiinDarkText
            )
        }

        // --- SCROLLABLE CONTENT ---
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // 1. Item Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                border = BorderStroke(1.dp, localBorderGrey)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(64.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF6B8E78)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Menu, contentDescription = null, tint = Color.White)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        Text(
                            text = "SCHEDULING FOR",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = BagiinGreyText,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = itemTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = BagiinDarkText,
                            lineHeight = 20.sp
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = BagiinGreen,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Donated by Alex Chen",
                                fontSize = 12.sp,
                                color = BagiinGreen,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 2. When Section
            Text(
                text = "When would you like to meet?",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BagiinDarkText
            )
            Spacer(modifier = Modifier.height(12.dp))

            // --- Date Picker Dialog ---
            if (showDatePicker) {
                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showDatePicker = false
                                datePickerState.selectedDateMillis?.let { millis ->
                                    val formatter = SimpleDateFormat("MM/dd/yyyy", Locale.getDefault())
                                    selectedDateText = formatter.format(Date(millis))
                                }
                            }
                        ) {
                            Text("OK", color = BagiinGreen)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel", color = BagiinDarkText)
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            // --- Time Picker Dialog ---
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
                                selectedTimeText = String.format(Locale.getDefault(), "%d:%02d %s", formattedHour, minute, amPm)
                            }
                        ) {
                            Text("OK", color = BagiinGreen)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showTimePicker = false }) {
                            Text("Cancel", color = BagiinDarkText)
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

            // Date Picker UI
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true },
                shape = RoundedCornerShape(8.dp),
                color = localGrey
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.DateRange, contentDescription = null, tint = BagiinDarkText)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = selectedDateText, fontSize = 15.sp, color = BagiinDarkText)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Time Picker UI
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showTimePicker = true },
                shape = RoundedCornerShape(8.dp),
                color = localGrey
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = selectedTimeText, fontSize = 15.sp, color = BagiinDarkText)
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(Icons.Default.KeyboardArrowDown, contentDescription = null, tint = BagiinDarkText)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Where Section
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Where will you meet?",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BagiinDarkText
                )
                Row(
                    modifier = Modifier.clickable { /* TODO: Change Location Map logic */ },
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Edit, contentDescription = null, tint = BagiinGreen, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Change", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = BagiinGreen)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Location Input TextField
            TextField(
                value = meetingLocation,
                onValueChange = { meetingLocation = it },
                placeholder = {
                    Text(
                        "e.g., Jl. Mawar 123",
                        color = BagiinGreyText,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = localGrey,
                    unfocusedContainerColor = localGrey,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 4. Additional Instructions Section
            Text(
                text = "Additional Instructions (Optional)",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = BagiinDarkText
            )
            Spacer(modifier = Modifier.height(12.dp))

            // Instructions Input TextField
            TextField(
                value = additionalInstructions,
                onValueChange = { additionalInstructions = it },
                placeholder = {
                    Text(
                        "e.g., 'I'll be wearing a red jacket' or 'Text me when you arrive'",
                        color = BagiinGreyText,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp),
                shape = RoundedCornerShape(12.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = localGrey,
                    unfocusedContainerColor = localGrey,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                )
            )

            Spacer(modifier = Modifier.height(24.dp))
        }

        // --- BOTTOM ACTION BUTTONS ---
        Surface(
            color = Color.White,
            shadowElevation = 8.dp,
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .weight(1f)
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, Color.Gray)
                ) {
                    Text("Cancel", color = BagiinDarkText, fontSize = 15.sp)
                }

                Button(
                    onClick = {
                        // Open the Success Dialog
                        showSuccessDialog = true
                    },
                    modifier = Modifier
                        .weight(1.5f)
                        .height(52.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BagiinGreen)
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Confirm Schedule", fontSize = 15.sp, fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}
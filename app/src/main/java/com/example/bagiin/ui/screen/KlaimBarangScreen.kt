package com.example.bagiin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlaimBarangScreen(navController: NavController, itemTitle: String) {
    var claimReason by remember { mutableStateOf("") }

    Scaffold(
        containerColor = LGBackground,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Klaim Barang",
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
                Button(
                    onClick = {
                        val encodedTitle = URLEncoder.encode(itemTitle, StandardCharsets.UTF_8.toString())
                        navController.navigate("jadwal_penyerahan/$encodedTitle")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 12.dp)
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LGPrimaryContainer
                    ),
                    elevation = ButtonDefaults.buttonElevation(
                        defaultElevation = 2.dp,
                        pressedElevation = 0.dp
                    ),
                    enabled = claimReason.isNotBlank()
                ) {
                    Text(
                        text = "Ajukan Klaim",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LGOnPrimary,
                        letterSpacing = 0.02.sp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(
                        Icons.AutoMirrored.Filled.Send,
                        contentDescription = null,
                        tint = LGOnPrimary,
                        modifier = Modifier.size(18.dp)
                    )
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
            Spacer(modifier = Modifier.height(20.dp))

            // ==================== ITEM SUMMARY CARD ====================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = LGSurfaceLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Item image placeholder
                        Box(
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(LGSurfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Image,
                                contentDescription = null,
                                tint = LGOutlineVariant,
                                modifier = Modifier.size(32.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            // Category chip
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = LGPrimaryContainer
                            ) {
                                Text(
                                    text = "Pakaian",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = LGOnPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                                )
                            }

                            Spacer(modifier = Modifier.height(6.dp))

                            // Item name
                            Text(
                                text = itemTitle,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LGOnBackground,
                                lineHeight = 20.sp
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            // Condition
                            Text(
                                text = "Kondisi: 90% (Sangat Baik)",
                                fontSize = 12.sp,
                                color = LGOnSurfaceVariant
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Divider
                    HorizontalDivider(color = LGOutlineVariant.copy(alpha = 0.4f))

                    Spacer(modifier = Modifier.height(12.dp))

                    // Location row
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            Icons.Outlined.LocationOn,
                            contentDescription = null,
                            tint = LGPrimaryContainer,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Jakarta Selatan",
                            fontSize = 13.sp,
                            color = LGOnSurfaceVariant,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ==================== ALASAN KLAIM SECTION ====================
            Text(
                text = "Alasan Klaim",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = LGOnBackground,
                letterSpacing = (-0.01).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Ceritakan mengapa Anda membutuhkan barang ini agar pendonor dapat memverifikasi.",
                fontSize = 14.sp,
                color = LGOnSurfaceVariant,
                lineHeight = 20.sp
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Text area
            OutlinedTextField(
                value = claimReason,
                onValueChange = { claimReason = it },
                placeholder = {
                    Text(
                        "Tuliskan cerita Anda di sini...",
                        color = LGOutline,
                        fontSize = 14.sp
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 160.dp),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = LGOutlineVariant,
                    focusedBorderColor = LGPrimaryContainer,
                    cursorColor = LGPrimaryContainer,
                    unfocusedContainerColor = LGSurfaceContainerLow,
                    focusedContainerColor = LGSurfaceLowest
                ),
                textStyle = LocalTextStyle.current.copy(
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    color = LGOnBackground
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            // ==================== INFO NOTICE ====================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = LGSurfaceContainerLow),
                elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        Icons.Outlined.Info,
                        contentDescription = null,
                        tint = LGPrimaryContainer,
                        modifier = Modifier.size(20.dp)
                    )

                    Text(
                        text = "Informasi ini hanya akan dibagikan kepada pemilik barang. Kejujuran Anda membantu komunitas tetap terpercaya.",
                        fontSize = 13.sp,
                        color = LGOnSurfaceVariant,
                        lineHeight = 19.sp,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
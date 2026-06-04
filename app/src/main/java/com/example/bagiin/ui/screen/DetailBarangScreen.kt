package com.example.bagiin.ui.screen

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Luminous Giving Colors (consistent with DashboardScreen)
private val LGPrimary = Color(0xFF004AC6)
private val LGPrimaryContainer = Color(0xFF2563EB)
private val LGOnPrimary = Color(0xFFFFFFFF)
private val LGBackground = Color(0xFFF8F9FF)
private val LGSurfaceLowest = Color(0xFFFFFFFF)
private val LGSurfaceContainer = Color(0xFFE5EEFF)
private val LGSurfaceContainerLow = Color(0xFFEFF4FF)
private val LGSurfaceContainerHigh = Color(0xFFDCE9FF)
private val LGOnBackground = Color(0xFF0B1C30)
private val LGOnSurfaceVariant = Color(0xFF434655)
private val LGOutline = Color(0xFF737686)
private val LGOutlineVariant = Color(0xFFC3C6D7)
private val LGSecondaryContainer = Color(0xFF64A8FE)
private val LGError = Color(0xFFBA1A1A)

// Status colors
private val StatusGreen = Color(0xFF16A34A)
private val StatusGreenBg = Color(0xFFDCFCE7)
private val StarYellow = Color(0xFFFBBF24)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailBarangScreen(
    navController: NavController,
    itemTitle: String,
    itemCategory: String = "Pakaian",
    itemCondition: String = "Sangat Baik",
    itemLocation: String = "Jakarta Selatan",
    itemDescription: String = "Satu set pakaian bayi lengkap untuk usia 0-6 bulan. Terdiri dari 5 pasang baju tidur, 3 setelan harian, dan perlengkapan lainnya. Semua bahan terbuat dari katun organik premium yang sangat lembut dan aman untuk kulit sensitif bayi.\n\nBarang ini baru digunakan beberapa kali dan masih dalam kondisi seperti baru, tanpa noda atau robekan. Sudah dicuci bersih menggunakan deterjen khusus bayi dan siap digunakan untuk yang membutuhkan.",
    donorName: String = "Siti Aminah",
    donorRating: Float = 4.9f,
    donorDonationCount: Int = 12,
    uploadedTime: String = "2 jam lalu"
) {
    var isFavorited by remember { mutableStateOf(false) }
    var currentImageIndex by remember { mutableIntStateOf(0) }
    val totalImages = 3

    Scaffold(
        containerColor = LGBackground,
        topBar = {
            // Transparent top bar overlay on image
        },
        bottomBar = {
            // Sticky bottom bar
            Surface(
                color = LGSurfaceLowest,
                shadowElevation = 12.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Favorite button
                    OutlinedIconButton(
                        onClick = { isFavorited = !isFavorited },
                        modifier = Modifier.size(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isFavorited) LGError else LGOutlineVariant
                        )
                    ) {
                        Icon(
                            imageVector = if (isFavorited) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                            contentDescription = "Favorite",
                            tint = if (isFavorited) LGError else LGOnSurfaceVariant,
                            modifier = Modifier.size(22.dp)
                        )
                    }

                    // Klaim Barang button
                    Button(
                        onClick = {
                            val encodedTitle = URLEncoder.encode(itemTitle, StandardCharsets.UTF_8.toString())
                            navController.navigate("klaim_barang/$encodedTitle")
                        },
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = LGPrimaryContainer
                        ),
                        elevation = ButtonDefaults.buttonElevation(
                            defaultElevation = 2.dp,
                            pressedElevation = 0.dp
                        )
                    ) {
                        Text(
                            text = "Klaim Barang",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = LGOnPrimary,
                            letterSpacing = 0.02.sp
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
        ) {
            // ==================== IMAGE SECTION ====================
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // Image placeholder with gradient background
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    LGSurfaceContainer,
                                    LGSurfaceContainerHigh
                                )
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Image,
                        contentDescription = null,
                        tint = LGOutlineVariant,
                        modifier = Modifier.size(64.dp)
                    )
                }

                // Top bar overlay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .statusBarsPadding()
                        .padding(horizontal = 8.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Back button
                    IconButton(
                        onClick = { navController.popBackStack() },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LGSurfaceLowest.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = LGOnBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    // Title
                    Text(
                        text = "Detail Barang",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LGOnBackground
                    )

                    // Share button
                    IconButton(
                        onClick = { /* Share action */ },
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(LGSurfaceLowest.copy(alpha = 0.9f))
                    ) {
                        Icon(
                            Icons.Outlined.Share,
                            contentDescription = "Share",
                            tint = LGOnBackground,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                // Status badge "Tersedia"
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LGPrimaryContainer,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 92.dp, end = 16.dp)
                ) {
                    Text(
                        text = "Tersedia",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LGOnPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                // Image indicator dots
                Row(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    repeat(totalImages) { index ->
                        Box(
                            modifier = Modifier
                                .size(if (index == currentImageIndex) 8.dp else 6.dp)
                                .clip(CircleShape)
                                .background(
                                    if (index == currentImageIndex) LGOnPrimary
                                    else LGOnPrimary.copy(alpha = 0.5f)
                                )
                                .clickable { currentImageIndex = index }
                        )
                    }
                }
            }

            // ==================== CONTENT SECTION ====================
            Column(
                modifier = Modifier.padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(16.dp))

                // Category chip + Condition
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Category chip
                    Surface(
                        shape = RoundedCornerShape(9999.dp),
                        color = LGSurfaceLowest,
                        border = androidx.compose.foundation.BorderStroke(1.dp, LGOutlineVariant)
                    ) {
                        Text(
                            text = itemCategory,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = LGOnSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    // Dot separator
                    Text(
                        text = "•",
                        fontSize = 14.sp,
                        color = LGOutline
                    )

                    // Condition label
                    Text(
                        text = "Kondisi: $itemCondition",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LGOnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Item title
                Text(
                    text = itemTitle,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = LGOnBackground,
                    lineHeight = 28.sp,
                    letterSpacing = (-0.01).sp
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Description section
                Text(
                    text = "Deskripsi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = LGOnBackground
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = itemDescription,
                    fontSize = 14.sp,
                    color = LGOnSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // ==================== DONATOR CARD ====================
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = LGSurfaceLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Avatar placeholder
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(LGSurfaceContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = LGOutline,
                                modifier = Modifier.size(28.dp)
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Donatur",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = LGOnSurfaceVariant,
                                letterSpacing = 0.02.sp
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = donorName,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LGOnBackground
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Icon(
                                    Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = StarYellow,
                                    modifier = Modifier.size(14.dp)
                                )
                                Text(
                                    text = "$donorRating ($donorDonationCount Donasi)",
                                    fontSize = 12.sp,
                                    color = LGOnSurfaceVariant,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // ==================== LOCATION & TIME INFO ====================
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Location card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LGSurfaceContainerLow),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Outlined.LocationOn,
                                contentDescription = null,
                                tint = LGPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Lokasi",
                                fontSize = 11.sp,
                                color = LGOnSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = itemLocation,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LGOnBackground,
                                textAlign = TextAlign.Center
                            )
                        }
                    }

                    // Time card
                    Card(
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = LGSurfaceContainerLow),
                        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                Icons.Outlined.AccessTime,
                                contentDescription = null,
                                tint = LGPrimaryContainer,
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Diunggah",
                                fontSize = 11.sp,
                                color = LGOnSurfaceVariant,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = uploadedTime,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LGOnBackground,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }

                // Bottom spacer for scroll breathing room
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

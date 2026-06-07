package com.example.bagiin.ui.screen

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bagiin.viewmodel.DonasiViewModel
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
private val LGSurfaceContainerHigh = Color(0xFFDCE9FF)
private val LGOnBackground = Color(0xFF0B1C30)
private val LGOnSurfaceVariant = Color(0xFF434655)
private val LGOutline = Color(0xFF737686)
private val LGOutlineVariant = Color(0xFFC3C6D7)
private val LGSecondaryContainer = Color(0xFF64A8FE)
private val LGError = Color(0xFFBA1A1A)

// Status colors
private val StarYellow = Color(0xFFFBBF24)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlaimBarangScreen(
    navController: NavController,
    idDonasi: String,
    itemTitle: String,
    viewModel: DonasiViewModel = viewModel()
) {
    var claimReason by remember { mutableStateOf("") }
    var isFavorited by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val donasiDetail = viewModel.donationDetail.value
    val isLoading = viewModel.isLoading.value

    LaunchedEffect(idDonasi) {
        viewModel.getDonationById(idDonasi)
    }

    Scaffold(
        containerColor = LGBackground,
        topBar = {
            // Transparent top bar overlay on image, handled inside column
        },
        bottomBar = {
            // Sticky bottom bar containing favorite and submit button
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
                        border = BorderStroke(
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

                    // Ajukan Klaim Button
                    Button(
                        onClick = {
                            viewModel.claimDonation(
                                idDonasi,
                                claimReason
                            ) { claimId ->

                                val finalTitle =
                                    donasiDetail?.judul ?: itemTitle

                                val encodedTitle =
                                    URLEncoder.encode(
                                        finalTitle,
                                        StandardCharsets.UTF_8.toString()
                                    )

                                navController.navigate(
                                    "jadwal_penyerahan/$claimId/$encodedTitle"
                                )
                            }
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
                        ),
                        enabled = claimReason.isNotBlank() && !isLoading
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp), color = LGOnPrimary)
                        } else {
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
            val imageUrls = donasiDetail?.foto_url ?: emptyList()
            val pagerState = rememberPagerState(pageCount = { if (imageUrls.isEmpty()) 1 else imageUrls.size })

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                // Image pager or placeholder
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { page ->
                    if (imageUrls.isNotEmpty()) {
                        AsyncImage(
                            model = imageUrls[page],
                            contentDescription = "Donation Image $page",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Gradient placeholder
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
                    }
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
                        text = "Detail & Klaim Barang",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = LGOnBackground,
                        modifier = Modifier
                            .clip(RoundedCornerShape(99.dp))
                            .background(LGSurfaceLowest.copy(alpha = 0.9f))
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    )

                    // Spacer to keep title centered
                    Spacer(modifier = Modifier.size(40.dp))
                }

                // Status badge "Tersedia" or current status
                val displayStatus = donasiDetail?.status?.replaceFirstChar { it.uppercase() } ?: "Tersedia"
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = LGPrimaryContainer,
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(top = 92.dp, end = 16.dp)
                ) {
                    Text(
                        text = displayStatus,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LGOnPrimary,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                // Image indicator dots
                if (imageUrls.size > 1) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(imageUrls.size) { index ->
                            Box(
                                modifier = Modifier
                                    .size(if (index == pagerState.currentPage) 8.dp else 6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        if (index == pagerState.currentPage) LGPrimary
                                        else Color.White.copy(alpha = 0.5f)
                                    )
                            )
                        }
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
                        border = BorderStroke(1.dp, LGOutlineVariant)
                    ) {
                        Text(
                            text = donasiDetail?.kategori ?: "Pakaian",
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
                        text = "Kondisi: ${donasiDetail?.kondisi ?: "Sangat Baik"}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = LGOnSurfaceVariant
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Item title
                Text(
                    text = donasiDetail?.judul ?: itemTitle,
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
                    text = donasiDetail?.deskripsi ?: "Tidak ada deskripsi.",
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
                        // Avatar placeholder or actual image
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(CircleShape)
                                .background(LGSurfaceContainer)
                        ) {
                            if (!donasiDetail?.donor?.foto_profil.isNullOrEmpty()) {
                                AsyncImage(
                                    model = donasiDetail?.donor?.foto_profil,
                                    contentDescription = "Donor Avatar",
                                    modifier = Modifier.fillMaxSize(),
                                    contentScale = ContentScale.Crop
                                )
                            } else {
                                Icon(
                                    Icons.Default.Person,
                                    contentDescription = null,
                                    tint = LGOutline,
                                    modifier = Modifier
                                        .size(28.dp)
                                        .align(Alignment.Center)
                                )
                            }
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
                                text = donasiDetail?.donor?.nama ?: "Pendonor BAGIIN",
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
                                    text = "${donasiDetail?.rating ?: 4.9} (12 Donasi)",
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
                                text = donasiDetail?.lokasi ?: "Jakarta Selatan",
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
                                text = "2 jam lalu", // Fallback or dynamic relative time format if needed
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LGOnBackground,
                                textAlign = TextAlign.Center
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

                // Text area for Claim Reason
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
                        .heightIn(min = 140.dp),
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

                Spacer(modifier = Modifier.height(20.dp))

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

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

package com.example.bagiin.ui.screen

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
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
private val LGOnBackground = Color(0xFF0B1C30)
private val LGOnSurfaceVariant = Color(0xFF434655)
private val LGOutline = Color(0xFF737686)
private val LGOutlineVariant = Color(0xFFC3C6D7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KlaimBarangScreen(
    navController: NavController,
    idDonasi: String,
    itemTitle: String,
    viewModel: DonasiViewModel = viewModel()
) {
    var claimReason by remember { mutableStateOf("") }
    val context = LocalContext.current
    val donasiDetail = viewModel.donationDetail.value
    val isLoading = viewModel.isLoading.value

    LaunchedEffect(idDonasi) {
        viewModel.getDonationById(idDonasi)
    }

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
                        viewModel.claimDonation(idDonasi, claimReason) {
                            Toast.makeText(context, "Klaim berhasil diajukan!", Toast.LENGTH_SHORT).show()
                            val encodedTitle = URLEncoder.encode(itemTitle, StandardCharsets.UTF_8.toString())
                            navController.navigate("jadwal_penyerahan/$encodedTitle") {
                                popUpTo("dashboard") { inclusive = false }
                            }
                        }
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
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // ==================== IMAGE CAROUSEL ====================
            val imageUrls = donasiDetail?.foto_url ?: emptyList()
            val pagerState = rememberPagerState(pageCount = { if (imageUrls.isEmpty()) 1 else imageUrls.size })
            
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(LGSurfaceContainer)
            ) {
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
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.Image, contentDescription = null, tint = LGOutlineVariant, modifier = Modifier.size(64.dp))
                        }
                    }
                }
                
                // Indicators
                if (imageUrls.size > 1) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        repeat(imageUrls.size) { iteration ->
                            val color = if (pagerState.currentPage == iteration) LGPrimary else Color.White.copy(alpha = 0.5f)
                            Box(
                                modifier = Modifier
                                    .size(8.dp)
                                    .clip(CircleShape)
                                    .background(color)
                            )
                        }
                    }
                }
            }

            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Spacer(modifier = Modifier.height(20.dp))

                // Item Basic Info
                Text(
                    text = donasiDetail?.judul ?: itemTitle,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = LGOnBackground
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(shape = RoundedCornerShape(6.dp), color = LGPrimaryContainer.copy(alpha = 0.1f)) {
                        Text(
                            text = donasiDetail?.kategori ?: "Kategori",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = LGPrimaryContainer,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Surface(shape = RoundedCornerShape(6.dp), color = Color(0xFFF59E0B).copy(alpha = 0.1f)) {
                        Text(
                            text = "Kondisi: ${donasiDetail?.kondisi ?: "-"}",
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                            color = Color(0xFFD97706),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // ==================== ITEM SPECIFICATIONS ====================
                Text("Detail Barang", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = LGOnBackground)
                Spacer(modifier = Modifier.height(12.dp))
                
                // Description
                Text(
                    text = donasiDetail?.deskripsi ?: "Tidak ada deskripsi.",
                    fontSize = 14.sp,
                    color = LGOnSurfaceVariant,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(20.dp))
                
                // Location Detail
                Row(verticalAlignment = Alignment.Top) {
                    Icon(Icons.Outlined.LocationOn, contentDescription = null, tint = LGPrimaryContainer, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text("Lokasi Penjemputan", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = LGOnBackground)
                        Text(donasiDetail?.lokasi ?: "-", fontSize = 13.sp, color = LGOnSurfaceVariant)
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // ==================== DONOR INFO SECTION ====================
                Text(
                    text = "Informasi Pendonor",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = LGOnBackground
                )

                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = LGSurfaceLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
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
                                    tint = LGOutlineVariant,
                                    modifier = Modifier.align(Alignment.Center)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.width(12.dp))
                        
                        Column {
                            Text(
                                text = donasiDetail?.donor?.nama ?: "Pendonor BAGIIN",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = LGOnBackground
                            )
                            Text(
                                text = "Pemilik barang",
                                fontSize = 12.sp,
                                color = LGOnSurfaceVariant
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

                Spacer(modifier = Modifier.height(32.dp))
            }
        }
    }
}

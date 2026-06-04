package com.example.bagiin.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bagiin.R
import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.model.DonationItem
import com.example.bagiin.viewmodel.ProfileViewModel
import io.github.jan.supabase.auth.auth
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

// Luminous Giving Colors
private val Primary = Color(0xFF004AC6)
private val PrimaryContainer = Color(0xFF2563EB)
private val OnPrimary = Color(0xFFFFFFFF)
private val Background = Color(0xFFF8F9FF)
private val SurfaceLowest = Color(0xFFFFFFFF)
private val SurfaceContainer = Color(0xFFE5EEFF)
private val OnBackground = Color(0xFF0B1C30)
private val OnSurfaceVariant = Color(0xFF434655)
private val OutlineVariant = Color(0xFFC3C6D7)
private val SecondaryContainer = Color(0xFF64A8FE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val user = profileViewModel.user.value
    val email = SupabaseInstance.client.auth.currentUserOrNull()?.email ?: ""
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Semua") }
    val categories = listOf(
        Pair("Semua", Icons.Default.AllInclusive),
        Pair("Pakaian", Icons.Default.Checkroom),
        Pair("Buku", Icons.AutoMirrored.Filled.MenuBook),
        Pair("Alat Sekolah", Icons.Default.School)
    )

    val allDonations = listOf(
        DonationItem("Paket Pakaian Bayi", "Jakarta Selatan", "Sangat Baik", "Pakaian"),
        DonationItem("Kumpulan Novel Remaja", "Bandung", "Sangat Baik", "Buku"),
        DonationItem("Mainan Kayu Edukasi", "Yogyakarta", "Baik", "Alat Sekolah"),
        DonationItem("Seragam SD Kelas 1", "Jakarta Timur", "Sangat Baik", "Pakaian"),
        DonationItem("Buku Pelajaran Matematika", "Surabaya", "Baik", "Buku"),
        DonationItem("Tas Sekolah Karakter", "Semarang", "Sangat Baik", "Alat Sekolah")
    )

    val filteredDonations = allDonations.filter { item ->
        (selectedCategory == "Semua" || item.category == selectedCategory) &&
        (searchQuery.isEmpty() || item.title.contains(searchQuery, ignoreCase = true))
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceLowest,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Primary,
                        selectedTextColor = Primary,
                        indicatorColor = SecondaryContainer.copy(alpha = 0.3f),
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("upload_donasi") },
                    icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = "Upload") },
                    label = { Text("Upload", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("riwayat_donasi") },
                    icon = { Icon(Icons.Outlined.History, contentDescription = "History") },
                    label = { Text("History", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant
                    )
                )
            }
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(bottom = 24.dp)
        ) {
            // Top Bar
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                // Removed Menu Icon as requested
                Spacer(modifier = Modifier.width(8.dp)) 

                Image(
                    painter = painterResource(id = R.drawable.ic_bagiin_logo),
                    contentDescription = "Logo Bagiin",
                    modifier = Modifier.height(32.dp),
                    contentScale = ContentScale.Fit
                )

                Spacer(modifier = Modifier.weight(1f))

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(OutlineVariant)
                ) {
                        if (!user?.foto_profil.isNullOrEmpty()) {
                            AsyncImage(
                                model = user?.foto_profil,
                                contentDescription = null,
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            // Profile placeholder
                            Icon(
                                Icons.Default.Person,
                                contentDescription = null,
                                tint = SurfaceLowest,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }
                }
            }

            // Greeting
            item {
                Text(
                    text = "Halo, ${user?.nama?.ifEmpty { "Budi" } ?: "Budi"}!",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnBackground,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Search Bar
            item {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Cari barang atau kebutuhan...", color = OnSurfaceVariant, fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = OnSurfaceVariant)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(9999.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = OutlineVariant,
                        focusedBorderColor = Primary,
                        cursorColor = Primary,
                        unfocusedContainerColor = SurfaceLowest,
                        focusedContainerColor = SurfaceLowest
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Categories
            item {
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(categories.size) { index ->
                        val category = categories[index]
                        val isSelected = category.first == selectedCategory
                        
                        Surface(
                            shape = RoundedCornerShape(9999.dp),
                            color = if (isSelected) Primary else SurfaceLowest,
                            border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, OutlineVariant) else null,
                            onClick = { selectedCategory = category.first }
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = category.second,
                                    contentDescription = null,
                                    tint = if (isSelected) OnPrimary else OnSurfaceVariant,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = category.first,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSelected) OnPrimary else OnSurfaceVariant
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Hero Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .height(160.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SecondaryContainer),
                    onClick = { navController.navigate("daftar_barang") }
                ) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        // Background placeholder color
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(SecondaryContainer)
                        )
                        Column(
                            modifier = Modifier
                                .align(Alignment.CenterStart)
                                .padding(24.dp)
                        ) {
                            Text(
                                text = "Berbagi Kebaikan",
                                fontSize = 12.sp,
                                color = OnPrimary,
                                fontWeight = FontWeight.Medium
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Donasi Barang yang\ntidak digunakan",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = OnPrimary,
                                lineHeight = 24.sp
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(9999.dp),
                                color = SurfaceLowest,
                                onClick = { navController.navigate("upload_donasi") }
                            ) {
                                Text(
                                    text = "Donasi",
                                    fontSize = 12.sp,
                                    color = Primary,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            // Section title
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Donasi Terbaru",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground
                    )
                    TextButton(onClick = { navController.navigate("daftar_barang") }) {
                        Text("Lihat Semua", color = Primary, fontSize = 14.sp)
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            // Donation Items
            items(filteredDonations) { item ->
                DonationItemCard(
                    title = item.title,
                    location = item.location,
                    condition = item.condition,
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                    onClick = {
                        val encodedTitle = URLEncoder.encode(item.title, StandardCharsets.UTF_8.toString())
                        navController.navigate("detail_barang/$encodedTitle")
                    }
                )
            }
            
            if (filteredDonations.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Barang tidak ditemukan",
                            color = OnSurfaceVariant,
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DonationItemCard(
    modifier: Modifier = Modifier,
    title: String,
    location: String,
    condition: String,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        onClick = onClick
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .background(SurfaceContainer)
            ) {
                // Image Placeholder
                Icon(
                    Icons.Default.Image,
                    contentDescription = null,
                    tint = OutlineVariant,
                    modifier = Modifier
                        .size(48.dp)
                        .align(Alignment.Center)
                )
                
                Surface(
                    shape = RoundedCornerShape(9999.dp),
                    color = SurfaceLowest,
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                ) {
                    Text(
                        text = condition,
                        fontSize = 11.sp,
                        color = Primary,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = title,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = OnBackground,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                    Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        Icons.Outlined.LocationOn,
                        contentDescription = "Location",
                        tint = OnSurfaceVariant,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = location,
                        fontSize = 14.sp,
                        color = OnSurfaceVariant
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = onClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(40.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Detail", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = OnPrimary)
                    }
                }
            }
        }
    }
}
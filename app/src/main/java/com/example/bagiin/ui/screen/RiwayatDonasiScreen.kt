package com.example.bagiin.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

// Colors from Luminous Giving
private val ColorSurfaceVariant = Color(0xFFE5EEFF)
private val ColorSecondaryContainer = Color(0xFF64A8FE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatDonasiScreen(navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(1) } // Default to "Klaim Saya"
    val tabs = listOf("Donasi Saya", "Klaim Saya")

    val klaimList = listOf(
        KlaimItem(
            title = "Mainan Edukasi Anak",
            status = "MENUNGGU",
            date = "12 Okt 2023",
            note = "Menunggu persetujuan donatur",
            noteIcon = Icons.Outlined.Info
        ),
        KlaimItem(
            title = "Buku Paket SMA XII",
            status = "BERHASIL",
            date = "05 Okt 2023",
            note = "Barang telah diterima dengan baik",
            noteIcon = null
        ),
        KlaimItem(
            title = "Jam Tangan Quartz",
            status = "MENUNGGU",
            date = "02 Okt 2023",
            note = "Menunggu persetujuan donatur",
            noteIcon = Icons.Outlined.Info
        )
    )

    Scaffold(
        containerColor = ColorBackground,
        bottomBar = {
            NavigationBar(
                containerColor = ColorSurface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("dashboard") },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = ColorOnSurfaceVariant,
                        unselectedTextColor = ColorOnSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("upload_donasi") },
                    icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = "Donate") },
                    label = { Text("Donate", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = ColorOnSurfaceVariant,
                        unselectedTextColor = ColorOnSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.History, contentDescription = "History") },
                    label = { Text("History", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = ColorSurface,
                        selectedTextColor = ColorPrimary,
                        indicatorColor = ColorPrimary,
                        unselectedIconColor = ColorOnSurfaceVariant,
                        unselectedTextColor = ColorOnSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = ColorOnSurfaceVariant,
                        unselectedTextColor = ColorOnSurfaceVariant
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Menu,
                        contentDescription = "Menu",
                        tint = ColorPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = "Riwayat",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorPrimary
                    )
                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Outlined.Notifications,
                        contentDescription = "Notifications",
                        tint = ColorOnSurfaceVariant,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(ColorOutlineVariant)
                    ) {
                        // Dummy avatar image
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = ColorSurface,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }
            }

            // Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = ColorBackground,
                contentColor = ColorPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = ColorPrimary,
                        height = 3.dp
                    )
                },
                divider = {
                    HorizontalDivider(color = ColorOutlineVariant.copy(alpha = 0.3f))
                }
            ) {
                tabs.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(
                                text = title,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = if (selectedTabIndex == index) ColorPrimary else ColorOnSurfaceVariant
                            )
                        }
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(8.dp))

            // Content
            if (selectedTabIndex == 1) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(klaimList) { item ->
                        KlaimCard(item)
                    }
                }
            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Donasi Saya - Kosong", color = ColorOnSurfaceVariant)
                }
            }
        }
    }
}

data class KlaimItem(
    val title: String,
    val status: String,
    val date: String,
    val note: String,
    val noteIcon: androidx.compose.ui.graphics.vector.ImageVector?
)

@Composable
fun KlaimCard(item: KlaimItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp) // Subtle or flat since background has color
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            // Image Box Placeholder
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(ColorOutlineVariant.copy(alpha = 0.3f))
            ) {
                Icon(
                    Icons.Outlined.Image,
                    contentDescription = null,
                    tint = ColorOnSurfaceVariant.copy(alpha = 0.5f),
                    modifier = Modifier.align(Alignment.Center)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Text(
                        text = item.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = ColorOnSurface,
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    // Status Badge
                    val isSuccess = item.status == "BERHASIL"
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = if (isSuccess) ColorPrimary else ColorSurfaceVariant,
                        modifier = Modifier.padding(top = 2.dp)
                    ) {
                        Text(
                            text = item.status,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSuccess) ColorSurface else ColorOnSurfaceVariant,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Klaim pada ${item.date}",
                    fontSize = 12.sp,
                    color = ColorOnSurfaceVariant
                )
                Spacer(modifier = Modifier.height(10.dp))
                Row(verticalAlignment = Alignment.Top) {
                    if (item.noteIcon != null) {
                        Icon(
                            imageVector = item.noteIcon,
                            contentDescription = null,
                            tint = ColorOnSurfaceVariant,
                            modifier = Modifier
                                .size(14.dp)
                                .padding(top = 2.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                    } else {
                        // Empty spacer for alignment if there is no icon
                        Spacer(modifier = Modifier.width(20.dp))
                    }
                    Text(
                        text = item.note,
                        fontSize = 13.sp,
                        color = ColorOnSurfaceVariant,
                        fontStyle = if (item.noteIcon != null) FontStyle.Italic else FontStyle.Normal
                    )
                }
            }
        }
    }
}
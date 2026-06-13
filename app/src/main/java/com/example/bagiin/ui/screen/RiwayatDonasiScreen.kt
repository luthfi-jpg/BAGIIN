package com.example.bagiin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import coil.compose.AsyncImage

private val ColorSurfaceVariant = Color(0xFFE5EEFF)
private val ColorSecondaryContainer = Color(0xFF64A8FE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RiwayatDonasiScreen(
    navController: NavController,
    viewModel: com.example.bagiin.viewmodel.RiwayatViewModel = androidx.lifecycle.viewmodel.compose.viewModel(),
    donasiViewModel: com.example.bagiin.viewmodel.DonasiViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
) {
    var selectedTabIndex by remember { mutableIntStateOf(1) }
    val tabs = listOf("Donasi Saya", "Klaim Saya")

    val myDonations = viewModel.myDonations.value
    val myClaims = viewModel.myClaims.value
    val isLoading = viewModel.isLoading.value

    var searchQuery by remember { mutableStateOf("") }
    var showRatingDialog by remember { mutableStateOf(false) }
    var selectedClaimForRating by remember { mutableStateOf<com.example.bagiin.model.Claim?>(null) }

    LaunchedEffect(Unit) {
        viewModel.fetchRiwayat()
    }

    LaunchedEffect(myDonations) {
        myDonations.forEach {
            android.util.Log.d("RIWAYAT_DEBUG", "judul=${it.judul}")
            android.util.Log.d("RIWAYAT_DEBUG", "foto=${it.foto_url}")
        }
    }

    if (showRatingDialog && selectedClaimForRating != null) {
        RatingDialog(
            onDismiss = {
                showRatingDialog = false
                selectedClaimForRating = null
            },
            onConfirm = { rating ->
                viewModel.confirmAndRate(
                    idKlaim = selectedClaimForRating!!.id_klaim ?: "",
                    idDonasi = selectedClaimForRating!!.id_donasi ?: "",
                    rating = rating,
                    onSuccess = {
                        showRatingDialog = false
                        selectedClaimForRating = null
                    }
                )
            }
        )
    }

    Scaffold(
        containerColor = ColorBackground,
        bottomBar = { RiwayatBottomBar(navController) }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            RiwayatTopBar()

            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = ColorBackground,
                contentColor = ColorPrimary,
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
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

            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
                placeholder = { Text("Cari donasi...") },
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            when {
                isLoading -> {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = ColorPrimary)
                    }
                }
                selectedTabIndex == 1 -> {
                    KlaimContent(
                        myClaims = myClaims,
                        onClaimClick = { claim ->
                            selectedClaimForRating = claim
                            showRatingDialog = true
                        }
                    )
                }
                else -> {
                    DonasiContent(
                        myDonations = myDonations,
                        searchQuery = searchQuery,
                        onDeleteClick = { item ->
                            donasiViewModel.deleteDonasi(item.id_donasi ?: "") {
                                viewModel.fetchRiwayat()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun RiwayatTopBar() {
    val profileViewModel: com.example.bagiin.viewmodel.ProfileViewModel =
        androidx.lifecycle.viewmodel.compose.viewModel()
    val user = profileViewModel.user.value

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "History",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = ColorPrimary
        )
        Box(
            modifier = Modifier
                .size(36.dp)
                .clip(CircleShape)
                .background(ColorOutlineVariant)
        ) {
            if (!user?.foto_profil.isNullOrEmpty()) {
                AsyncImage(
                    model = user?.foto_profil,
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    tint = ColorSurface,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
private fun RiwayatBottomBar(navController: NavController) {
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

@Composable
private fun KlaimContent(
    myClaims: List<com.example.bagiin.model.Claim>,
    onClaimClick: (com.example.bagiin.model.Claim) -> Unit
) {
    if (myClaims.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada klaim", color = ColorOnSurfaceVariant)
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(myClaims) { item ->

                val donasi = item.donasi
                val imageUrl = donasi?.foto_url?.firstOrNull()

                KlaimCard(
                    item = KlaimItem(
                        title = donasi?.judul
                            ?: "Klaim Barang #${item.id_donasi?.take(5) ?: "???"}",

                        status = item.status?.uppercase() ?: "PENDING",

                        date = item.created_at?.split("T")?.first() ?: "-",

                        note = item.alasan ?: "Tidak ada alasan",

                        noteIcon = Icons.Outlined.Info,

                        photoUrl = imageUrl
                    ),
                    modifier = Modifier.clickable(
                        enabled = item.status?.lowercase() == "pending"
                    ) {
                        onClaimClick(item)
                    }
                )
            }
        }
    }
}

@Composable
private fun DonasiContent(
    myDonations: List<com.example.bagiin.model.Donasi>,
    searchQuery: String,
    onDeleteClick: (com.example.bagiin.model.Donasi) -> Unit
) {
    if (myDonations.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Belum ada donasi", color = ColorOnSurfaceVariant)
        }
    } else {
        val filteredDonations = myDonations.filter {
            searchQuery.isBlank() ||
                    (it.judul ?: "").contains(searchQuery, ignoreCase = true) ||
                    (it.status ?: "").contains(searchQuery, ignoreCase = true) ||
                    (it.lokasi ?: "").contains(searchQuery, ignoreCase = true)
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(filteredDonations) { item ->
                KlaimCard(
                    item = KlaimItem(
                        title = item.judul ?: "Tanpa Judul",
                        status = item.status?.uppercase() ?: "TERSEDIA",
                        date = item.created_at?.split("T")?.first() ?: "-",
                        note = item.lokasi ?: "Lokasi tidak ada",
                        noteIcon = Icons.Outlined.LocationOn,
                        photoUrl = item.foto_url?.firstOrNull()
                    ),
                    showDeleteButton = item.status?.lowercase() == "tersedia",
                    onDeleteClick = { onDeleteClick(item) }
                )
            }
        }
    }
}

data class KlaimItem(
    val title: String,
    val status: String,
    val date: String,
    val note: String,
    val noteIcon: ImageVector?,
    val photoUrl: String? = null
)

@Composable
fun KlaimCard(
    item: KlaimItem,
    modifier: Modifier = Modifier,
    showDeleteButton: Boolean = false,
    onDeleteClick: () -> Unit = {}
) {
    var showDeleteDialog by remember { mutableStateOf(false) }

    if (showDeleteDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteDialog = false },
            title = { Text("Hapus Donasi") },
            text = { Text("Apakah Anda yakin ingin menghapus donasi ini?") },
            confirmButton = {
                TextButton(onClick = {
                    onDeleteClick()
                    showDeleteDialog = false
                }) {
                    Text("Hapus", color = Color.Red)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteDialog = false }) {
                    Text("Batal")
                }
            }
        )
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = ColorSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.Top
            ) {
                if (!item.photoUrl.isNullOrEmpty()) {
                    AsyncImage(
                        model = item.photoUrl,
                        contentDescription = null,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(ColorOutlineVariant.copy(alpha = 0.3f))
                    ) {
                        Icon(
                            Icons.Outlined.Image,
                            contentDescription = null,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
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
                        val isSuccess = item.status in listOf("BERHASIL", "DITERIMA", "SELESAI")
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
                        text = "Tanggal: ${item.date}",
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

            if (showDeleteButton) {
                OutlinedButton(
                    onClick = { showDeleteDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Hapus Donasi", fontSize = 12.sp)
                }
            }
        }
    }
}

@Composable
fun RatingDialog(
    onDismiss: () -> Unit,
    onConfirm: (Double) -> Unit
) {
    var rating by remember { mutableDoubleStateOf(5.0) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(16.dp),
            color = ColorSurface
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text("Beri Rating Donatur", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                Spacer(modifier = Modifier.height(16.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    for (i in 1..5) {
                        Icon(
                            imageVector = if (i <= rating) Icons.Default.Star else Icons.Default.StarBorder,
                            contentDescription = null,
                            tint = if (i <= rating) Color(0xFFFFB400) else ColorOutlineVariant,
                            modifier = Modifier
                                .size(40.dp)
                                .clickable { rating = i.toDouble() }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp)
                    ) { Text("Batal") }
                    Button(
                        onClick = { onConfirm(rating) },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary)
                    ) { Text("Simpan") }
                }
            }
        }
    }
}
package com.example.bagiin.ui.screen

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import java.util.Locale
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bagiin.R
import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.viewmodel.ProfileViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

// Luminous Giving Colors
private val Primary = Color(0xFF004AC6)
private val OnPrimary = Color(0xFFFFFFFF)
private val Background = Color(0xFFF8F9FF)
private val SurfaceLowest = Color(0xFFFFFFFF)
private val SurfaceContainerLow = Color(0xFFEFF4FF)
private val OnBackground = Color(0xFF0B1C30)
private val OnSurfaceVariant = Color(0xFF434655)
private val OutlineVariant = Color(0xFFC3C6D7)
private val Error = Color(0xFFBA1A1A)
private val SecondaryContainer = Color(0xFF64A8FE)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val user = viewModel.user.value
    val message = viewModel.message.value
    val loading = viewModel.loading.value
    val coroutineScope = rememberCoroutineScope()
    val email = SupabaseInstance.client.auth.currentUserOrNull()?.email ?: ""
    val context = LocalContext.current

    val imagePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            val inputStream = context.contentResolver.openInputStream(it)
            val byteArray = inputStream?.readBytes()
            if (byteArray != null) {
                viewModel.uploadAvatar(byteArray, "avatar_${System.currentTimeMillis()}.jpg")
            }
        }
    }

    var showEditProfileDialog by remember { mutableStateOf(false) }
    var showAddressDialog by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    if (showEditProfileDialog) {
        EditProfileDialog(
            currentNama = user?.nama ?: "",
            currentNoHp = user?.no_hp ?: "",
            onDismiss = { showEditProfileDialog = false },
            onSave = { nama, noHp ->
                viewModel.updateProfile(nama, noHp, user?.alamat ?: "")
                showEditProfileDialog = false
            }
        )
    }

    if (showAddressDialog) {
        EditAddressDialog(
            currentAlamat = user?.alamat ?: "",
            onDismiss = { showAddressDialog = false },
            onSave = { alamat ->
                viewModel.updateProfile(user?.nama ?: "", user?.no_hp ?: "", alamat)
                showAddressDialog = false
            }
        )
    }

    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Logout", fontWeight = FontWeight.Bold, color = OnBackground) },
            text = { Text("Are you sure you want to sign out?", color = OnSurfaceVariant) },
            confirmButton = {
                Button(
                    onClick = {
                        coroutineScope.launch {
                            SupabaseInstance.client.auth.signOut()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Error),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Logout", color = Color.White)
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = { showLogoutDialog = false },
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = OnBackground)
                ) {
                    Text("Cancel")
                }
            },
            shape = RoundedCornerShape(16.dp),
            containerColor = SurfaceLowest
        )
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavigationBar(
                containerColor = SurfaceLowest,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("dashboard") },
                    icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("upload_donasi") },
                    icon = { Icon(Icons.Outlined.AddCircleOutline, contentDescription = "Donate") },
                    label = { Text("Donate", fontSize = 11.sp) },
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
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Primary,
                        selectedTextColor = Primary,
                        indicatorColor = SecondaryContainer.copy(alpha = 0.3f),
                        unselectedIconColor = OnSurfaceVariant,
                        unselectedTextColor = OnSurfaceVariant
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Top bar
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
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Big Avatar
            Box(contentAlignment = Alignment.BottomEnd) {
                Box(
                    modifier = Modifier
                        .size(100.dp)
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
                        Icon(
                            Icons.Default.Person,
                            contentDescription = null,
                            tint = SurfaceLowest,
                            modifier = Modifier
                                .size(80.dp)
                                .align(Alignment.Center)
                        )
                    }
                }
                
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .offset(x = (-4).dp, y = (-4).dp)
                        .clickable { imagePickerLauncher.launch("image/*") },
                    shape = CircleShape,
                    color = Primary
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = "Change Photo",
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (loading) {
                CircularProgressIndicator(color = Primary, modifier = Modifier.size(24.dp))
            } else {
                Text(
                    text = user?.nama?.ifEmpty { "Budi Santoso" } ?: "Budi Santoso",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = OnBackground
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = email.ifEmpty { "budi.santoso@email.com" },
                    fontSize = 14.sp,
                    color = OnSurfaceVariant
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Stats Cards
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Donations Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Outlined.VolunteerActivism, 
                            contentDescription = "Donations", 
                            tint = Primary, 
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = viewModel.donationCount.intValue.toString(), 
                            fontSize = 18.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Primary
                        )
                        Text("Donations", fontSize = 12.sp, color = OnSurfaceVariant)
                    }
                }

                // Rating Card
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .height(110.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Outlined.Star, 
                            contentDescription = "Rating", 
                            tint = Color(0xFFFFB400), 
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = String.format(Locale.US, "%.1f/5.0", user?.rating ?: 0.0),
                            fontSize = 18.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = Primary
                        )
                        Text("Rating Profil", fontSize = 12.sp, color = OnSurfaceVariant)
                        Text("Berdasarkan donasi", fontSize = 10.sp, color = OutlineVariant)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Menu List
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column {
                    ProfileMenuItem(
                        icon = Icons.Outlined.Person,
                        title = "Edit Profil",
                        onClick = { showEditProfileDialog = true }
                    )
                    Divider(color = Background, modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuItem(
                        icon = Icons.Outlined.LocationOn,
                        title = "Alamat Saya",
                        onClick = { showAddressDialog = true }
                    )
                    Divider(color = Background, modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuItem(
                        icon = Icons.Outlined.Logout,
                        title = "Logout",
                        titleColor = Error,
                        iconTint = Error,
                        onClick = { showLogoutDialog = true }
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Footer
            Text(
                text = "Bagiin v1.2.4 • 2024 Human-Centered Transparency",
                fontSize = 11.sp,
                color = OutlineVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 32.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    title: String,
    onClick: () -> Unit,
    titleColor: Color = OnBackground,
    iconTint: Color = OnSurfaceVariant
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 20.dp, vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(title, fontSize = 16.sp, color = titleColor, modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = OutlineVariant)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileDialog(
    currentNama: String,
    currentNoHp: String,
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var nama by remember { mutableStateOf(currentNama) }
    var noHp by remember { mutableStateOf(currentNoHp) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Edit Profil", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnBackground)
                Text("Update your personal details", fontSize = 13.sp, color = OnSurfaceVariant)

                Spacer(modifier = Modifier.height(20.dp))

                val textFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedContainerColor = SurfaceContainerLow,
                    disabledContainerColor = SurfaceContainerLow,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )

                Text("Full Name", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = OnBackground)
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    value = nama,
                    onValueChange = { nama = it },
                    placeholder = { Text("Your name", color = OnSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text("Phone Number", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = OnBackground)
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    value = noHp,
                    onValueChange = { noHp = it },
                    placeholder = { Text("+62 000-0000-0000", color = OnSurfaceVariant) },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = textFieldColors
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(9999.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OnBackground)
                    ) {
                        Text("Cancel", fontSize = 14.sp)
                    }
                    Button(
                        onClick = { onSave(nama, noHp) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(9999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Save", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditAddressDialog(
    currentAlamat: String,
    onDismiss: () -> Unit,
    onSave: (String) -> Unit
) {
    var alamat by remember { mutableStateOf(currentAlamat) }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(modifier = Modifier.padding(24.dp)) {
                Text("Alamat Saya", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = OnBackground)
                Text("Manage pickup and delivery spots", fontSize = 13.sp, color = OnSurfaceVariant)

                Spacer(modifier = Modifier.height(20.dp))

                Text("Address", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = OnBackground)
                Spacer(modifier = Modifier.height(6.dp))
                TextField(
                    value = alamat,
                    onValueChange = { alamat = it },
                    placeholder = { Text("Enter your address", color = OnSurfaceVariant) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = SurfaceContainerLow,
                        unfocusedContainerColor = SurfaceContainerLow,
                        disabledContainerColor = SurfaceContainerLow,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                    )
                )

                Spacer(modifier = Modifier.height(24.dp))

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(9999.dp),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = OnBackground)
                    ) {
                        Text("Cancel", fontSize = 14.sp)
                    }
                    Button(
                        onClick = { onSave(alamat) },
                        modifier = Modifier.weight(1f).height(44.dp),
                        shape = RoundedCornerShape(9999.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Primary)
                    ) {
                        Text("Save", fontSize = 14.sp)
                    }
                }
            }
        }
    }
}
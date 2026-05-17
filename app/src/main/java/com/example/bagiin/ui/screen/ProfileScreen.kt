package com.example.bagiin.ui.screen

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.viewmodel.ProfileViewModel
import io.github.jan.supabase.auth.auth
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel = viewModel()
) {
    val user = viewModel.user.value
    val coroutineScope = rememberCoroutineScope()
    val email = SupabaseInstance.client.auth.currentUserOrNull()?.email ?: ""

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("dashboard") },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = BagiinGreen,
                        indicatorColor = BagiinGreenLight
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Upload") },
                    label = { Text("Upload", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.List, contentDescription = "History") },
                    label = { Text("History", fontSize = 11.sp) }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = BagiinGreen,
                        indicatorColor = BagiinGreen
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = BagiinGreen, modifier = Modifier.size(24.dp))
                Text("Bagiin", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = BagiinGreen)
                Icon(Icons.Default.Notifications, contentDescription = null, tint = BagiinDarkText, modifier = Modifier.size(24.dp))
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Profile card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Avatar
                    Box {
                        Surface(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(CircleShape),
                            color = BagiinGreenLight
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = email.first().uppercaseChar().toString(),
                                    fontSize = 36.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = BagiinGreen
                                )
                            }
                        }
                        Surface(
                            modifier = Modifier
                                .size(28.dp)
                                .align(Alignment.BottomEnd),
                            shape = CircleShape,
                            color = BagiinGreen
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Edit,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = user?.nama ?: email.substringBefore("@"),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = BagiinDarkText
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = BagiinGreyText, modifier = Modifier.size(14.dp))
                        Text(
                            text = user?.alamat?.ifEmpty { "Jakarta, Indonesia" } ?: "Jakarta, Indonesia",
                            fontSize = 13.sp,
                            color = BagiinGreyText
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Stats
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("24", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BagiinGreen)
                            Text("Donations", fontSize = 12.sp, color = BagiinGreyText)
                        }
                        Divider(
                            modifier = Modifier
                                .height(40.dp)
                                .width(1.dp),
                            color = Color(0xFFE0E0E0)
                        )
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text("12", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = BagiinGreen)
                            Text("Received", fontSize = 12.sp, color = BagiinGreyText)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Menu items
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(modifier = Modifier.padding(8.dp)) {
                    ProfileMenuItem(
                        icon = Icons.Default.Person,
                        iconBg = Color(0xFFE8F5E9),
                        iconTint = BagiinGreen,
                        title = "Edit Profile",
                        subtitle = "Update your personal details",
                        onClick = { viewModel.isEditing.value = true }
                    )
                    Divider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuItem(
                        icon = Icons.Default.LocationOn,
                        iconBg = Color(0xFFE3F2FD),
                        iconTint = Color(0xFF1976D2),
                        title = "My Addresses",
                        subtitle = "Manage pickup and delivery spots",
                        onClick = { }
                    )
                    Divider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuItem(
                        icon = Icons.Default.Notifications,
                        iconBg = Color(0xFFFFF3E0),
                        iconTint = Color(0xFFF57C00),
                        title = "Notification Settings",
                        subtitle = "Control your alerts and updates",
                        onClick = { }
                    )
                    Divider(color = Color(0xFFF5F5F5), modifier = Modifier.padding(horizontal = 16.dp))
                    ProfileMenuItem(
                        icon = Icons.Default.ExitToApp,
                        iconBg = Color(0xFFFFEBEE),
                        iconTint = Color(0xFFD32F2F),
                        title = "Logout",
                        subtitle = "Sign out securely",
                        titleColor = Color(0xFFD32F2F),
                        showArrow = false,
                        onClick = {
                            coroutineScope.launch {
                                SupabaseInstance.client.auth.signOut()
                                navController.navigate("login") {
                                    popUpTo(0) { inclusive = true }
                                }
                            }
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ProfileMenuItem(
    icon: ImageVector,
    iconBg: Color,
    iconTint: Color,
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    titleColor: Color = BagiinDarkText,
    showArrow: Boolean = true
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = iconBg,
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = iconTint, modifier = Modifier.size(22.dp))
            }
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(title, fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = titleColor)
            Text(subtitle, fontSize = 12.sp, color = BagiinGreyText)
        }

        if (showArrow) {
            Icon(Icons.Default.ChevronRight, contentDescription = null, tint = BagiinGreyText)
        }
    }
}
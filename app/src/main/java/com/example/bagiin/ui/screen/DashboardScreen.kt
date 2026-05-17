package com.example.bagiin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.ui.theme.*
import io.github.jan.supabase.auth.auth

@Composable
fun DashboardScreen(navController: NavController) {
    val email = SupabaseInstance.client.auth.currentUserOrNull()?.email ?: ""
    val nama = email.substringBefore("@")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BagiinGrey)
    ) {
        // Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(BagiinGreen)
                .padding(24.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Halo, $nama 👋",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Text(
                        text = "Ayo mulai berbagi hari ini!",
                        fontSize = 13.sp,
                        color = BagiinGreenLight
                    )
                }
                IconButton(onClick = { navController.navigate("profile") }) {
                    Surface(
                        shape = CircleShape,
                        color = BagiinGreenMid,
                        modifier = Modifier.size(40.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (email.isNotEmpty()) email.first().uppercaseChar().toString() else "?",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Menu Utama",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = BagiinDarkText
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashMenuCard(modifier = Modifier.weight(1f), icon = Icons.Default.Add, title = "Upload\nDonasi", onClick = {})
                DashMenuCard(modifier = Modifier.weight(1f), icon = Icons.Default.List, title = "Daftar\nBarang", onClick = {})
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashMenuCard(modifier = Modifier.weight(1f), icon = Icons.Default.ShoppingCart, title = "Klaim\nBarang", onClick = {})
                DashMenuCard(modifier = Modifier.weight(1f), icon = Icons.Default.DateRange, title = "Riwayat\nDonasi", onClick = {})
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                DashMenuCard(modifier = Modifier.weight(1f), icon = Icons.Default.DateRange, title = "Jadwal\nPenyerahan", onClick = {})
                DashMenuCard(modifier = Modifier.weight(1f), icon = Icons.Default.Person, title = "Profil\nSaya", onClick = { navController.navigate("profile") })
            }
        }
    }
}

@Composable
fun DashMenuCard(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    title: String,
    onClick: () -> Unit
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = RoundedCornerShape(12.dp),
                color = BagiinGreenLight,
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = BagiinGreen,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = BagiinDarkText,
                textAlign = TextAlign.Center,
                lineHeight = 16.sp
            )
        }
    }
}
package com.example.bagiin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }
    val categories = listOf("All", "Clothing", "Books", "Electronics", "Food", "Others")

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = Color.White,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = { },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color.White,
                        selectedTextColor = BagiinGreen,
                        indicatorColor = BagiinGreen
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.Add, contentDescription = "Upload") },
                    label = { Text("Upload", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = BagiinGreyText
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { },
                    icon = { Icon(Icons.Default.List, contentDescription = "History") },
                    label = { Text("History", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = BagiinGreyText
                    )
                )
                NavigationBarItem(
                    selected = false,
                    onClick = { navController.navigate("profile") },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
                    label = { Text("Profile", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = BagiinGreyText
                    )
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Top Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.Favorite,
                    contentDescription = null,
                    tint = BagiinGreen,
                    modifier = Modifier.size(28.dp)
                )
                Text(
                    text = "Bagiin",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = BagiinGreen
                )
                Icon(
                    Icons.Default.Notifications,
                    contentDescription = null,
                    tint = BagiinDarkText,
                    modifier = Modifier.size(24.dp)
                )
            }

            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search for donations...", color = BagiinGreyText, fontSize = 14.sp) },
                    leadingIcon = {
                        Icon(Icons.Default.Search, contentDescription = null, tint = BagiinGreyText)
                    },
                    modifier = Modifier.weight(1f),
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color(0xFFE0E0E0),
                        focusedBorderColor = BagiinGreen,
                        cursorColor = BagiinGreen,
                        unfocusedContainerColor = BagiinGrey,
                        focusedContainerColor = Color.White
                    )
                )
                IconButton(
                    onClick = { },
                    modifier = Modifier
                        .size(52.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(BagiinGrey)
                ) {
                    Icon(Icons.Default.Menu, contentDescription = null, tint = BagiinDarkText)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Categories
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(categories.size) { index ->
                    val category = categories[index]
                    val isSelected = category == selectedCategory
                    FilterChip(
                        selected = isSelected,
                        onClick = { selectedCategory = category },
                        label = {
                            Text(
                                text = category,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = BagiinGreen,
                            selectedLabelColor = Color.White,
                            containerColor = BagiinGrey,
                            labelColor = BagiinDarkText
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = isSelected,
                            borderColor = Color.Transparent,
                            selectedBorderColor = Color.Transparent
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Featured Item
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .height(220.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF2C5F4A)),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color(0xFF2C5F4A))
                    )
                    Column(
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(16.dp)
                    ) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color.White.copy(alpha = 0.2f)
                        ) {
                            Text(
                                text = "Like New",
                                fontSize = 11.sp,
                                color = Color.White,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Warm Winter Coat",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = "Men's size L, very warm.",
                            fontSize = 13.sp,
                            color = Color.White.copy(alpha = 0.8f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Grid Items
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    DonationItemCard(
                        modifier = Modifier.weight(1f),
                        title = "Fiction Books",
                        description = "Bundle of 5 books.",
                        condition = "Good",
                        conditionColor = BagiinGreyText
                    )
                    DonationItemCard(
                        modifier = Modifier.weight(1f),
                        title = "Sony Headphones",
                        description = "Working perfectly.",
                        condition = "Used",
                        conditionColor = Color(0xFF1976D2)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DonationItemCard(
    modifier: Modifier = Modifier,
    title: String,
    description: String,
    condition: String,
    conditionColor: Color
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = BagiinGrey),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color(0xFF2C5F4A))
            )
            Column(modifier = Modifier.padding(10.dp)) {
                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BagiinDarkText
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = BagiinGreyText
                )
                Spacer(modifier = Modifier.height(6.dp))
                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = conditionColor.copy(alpha = 0.1f)
                ) {
                    Text(
                        text = condition,
                        fontSize = 11.sp,
                        color = conditionColor,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                    )
                }
            }
        }
    }
}
package com.example.bagiin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.bagiin.ui.theme.*
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun KlaimBarangScreen(navController: NavController, itemTitle: String) {
    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = BagiinGreen)
            }
            Text(
                "Klaim Barang Donasi",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BagiinDarkText
            )
        }

        Box(contentAlignment = Alignment.Center) {
            Text("Fitur Klaim Barang\n(Dalam Pengembangan)", color = BagiinGreyText)
        }

        Button(
            onClick = {
                // UPDATE: Encode and pass to the next screen
                val encodedTitle = URLEncoder.encode(itemTitle, StandardCharsets.UTF_8.toString())
                navController.navigate("jadwal_penyerahan/$encodedTitle")
            },

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),

            colors = ButtonDefaults.buttonColors(
                containerColor = BagiinGreen
            ),
            ) {

            Text(
                text = "Claim This Item"
            )
        }
    }
}
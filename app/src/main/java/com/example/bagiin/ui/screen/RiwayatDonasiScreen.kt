package com.example.bagiin.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bagiin.ui.theme.*
import com.example.bagiin.viewmodel.HistoryViewModel

@Composable
fun RiwayatDonasiScreen(
    navController: NavController,
    viewModel: HistoryViewModel = viewModel()
) {

    val history by viewModel.history.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {

        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { navController.popBackStack() }
            ) {
                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = BagiinGreen
                )
            }

            Text(
                text = "Riwayat Donasi",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = BagiinDarkText
            )
        }

        if (history.isEmpty()) {

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Belum ada riwayat donasi",
                    color = BagiinGreyText
                )
            }

        } else {

            LazyColumn(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {

                items(history) { item ->

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {

                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {

                            Text(
                                text = item.aktivitas,
                                fontWeight = FontWeight.SemiBold
                            )

                            Spacer(
                                modifier = Modifier.height(4.dp)
                            )

                            Text(
                                text = item.tanggal,
                                color = BagiinGreyText
                            )
                        }
                    }
                }
            }
        }
    }
}
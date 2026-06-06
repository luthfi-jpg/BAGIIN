package com.example.bagiin.ui.screen

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.bagiin.viewmodel.DonasiViewModel

val ColorPrimary = Color(0xFF2563EB)
val ColorBackground = Color(0xFFF8F9FF)
val ColorSurface = Color(0xFFFFFFFF)
val ColorOnSurface = Color(0xFF0B1C30)
val ColorOnSurfaceVariant = Color(0xFF434655)
val ColorOutlineVariant = Color(0xFFC3C6D7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadDonasiScreen(
    navController: NavController,
    viewModel: DonasiViewModel = viewModel()
) {
    var selectedImageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }
    var namaBarang by remember { mutableStateOf("") }
    var kategori by remember { mutableStateOf("") }
    var expandedKategori by remember { mutableStateOf(false) }
    var kondisi by remember { mutableStateOf("") }
    var deskripsi by remember { mutableStateOf("") }
    var lokasi by remember { mutableStateOf("") }

    val context = LocalContext.current
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    val kondisiOptions = listOf("Baru", "Seperti Baru", "Bagus", "Layak Pakai")
    val kategoriOptions = listOf("Pakaian", "Elektronik", "Buku", "Mainan", "Lainnya")

    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickMultipleVisualMedia(5)
    ) { uris ->
        if (uris.isNotEmpty()) {
            val newUris = (selectedImageUris + uris).take(5)
            selectedImageUris = newUris
        }
    }

    Scaffold(
        containerColor = ColorBackground,
        topBar = {
            TopAppBar(
                title = { Text("Donasikan Barang", fontWeight = FontWeight.SemiBold, color = ColorOnSurface, fontSize = 18.sp) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back", tint = ColorOnSurface)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = ColorBackground)
            )
        },
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorSurface)
                    .padding(16.dp)
            ) {
                Button(
                    onClick = {
                        if (namaBarang.isBlank() || kategori.isBlank() || kondisi.isBlank() || lokasi.isBlank()) {
                            Toast.makeText(context, "Harap lengkapi semua data", Toast.LENGTH_SHORT).show()
                            return@Button
                        }
                        
                        val imageByteArrays = selectedImageUris.mapNotNull { uri ->
                            context.contentResolver.openInputStream(uri)?.use { it.readBytes() }
                        }

                        viewModel.uploadDonasi(
                            judul = namaBarang,
                            deskripsi = deskripsi,
                            kategori = kategori,
                            kondisi = kondisi,
                            lokasi = lokasi,
                            imageByteArrays = imageByteArrays,
                            onSuccess = {
                                Toast.makeText(context, "Donasi berhasil dikirim!", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = ColorPrimary),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = ColorSurface)
                    } else {
                        Icon(Icons.Default.VolunteerActivism, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Submit Donasi", fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Foto Barang
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Foto Barang", fontWeight = FontWeight.SemiBold, color = ColorOnSurface, fontSize = 14.sp)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selectedImageUris.size < 5) {
                        item {
                            val stroke = Stroke(width = 3f, pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 15f), 0f))
                            Box(
                                modifier = Modifier
                                    .size(90.dp)
                                    .drawBehind {
                                        drawRoundRect(
                                            color = ColorOutlineVariant,
                                            style = stroke,
                                            cornerRadius = CornerRadius(16.dp.toPx())
                                        )
                                    }
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(ColorPrimary.copy(alpha = 0.05f))
                                    .clickable {
                                        photoPickerLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.CameraAlt, contentDescription = "Tambah Foto", tint = ColorOnSurfaceVariant)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text("Tambah", fontSize = 12.sp, color = ColorOnSurfaceVariant)
                                }
                            }
                        }
                    }
                    items(selectedImageUris) { uri ->
                        Box(
                            modifier = Modifier
                                .size(90.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color(0xFFE2E8F0)) // fallback
                        ) {
                            AsyncImage(
                                model = uri,
                                contentDescription = "Selected Image",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            IconButton(
                                onClick = { selectedImageUris = selectedImageUris - uri },
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(24.dp)
                                    .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Remove", tint = Color.White, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
                Text("Minimal 1 foto, maksimal 5 foto.", fontSize = 13.sp, color = ColorOnSurfaceVariant, fontStyle = FontStyle.Italic)
            }

            // Nama Barang
            InputField(
                label = "Nama Barang",
                value = namaBarang,
                onValueChange = { namaBarang = it },
                placeholder = "Contoh: Sepatu Lari Ukuran 42"
            )

            // Kategori
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Kategori", fontWeight = FontWeight.SemiBold, color = ColorOnSurface, fontSize = 14.sp)
                ExposedDropdownMenuBox(
                    expanded = expandedKategori,
                    onExpandedChange = { expandedKategori = !expandedKategori }
                ) {
                    OutlinedTextField(
                        value = if (kategori.isEmpty()) "Pilih kategori barang" else kategori,
                        onValueChange = {},
                        readOnly = true,
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedKategori) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = ColorOutlineVariant,
                            focusedBorderColor = ColorPrimary,
                            unfocusedContainerColor = ColorSurface,
                            focusedContainerColor = ColorSurface,
                            unfocusedTextColor = if (kategori.isEmpty()) ColorOnSurfaceVariant else ColorOnSurface,
                            focusedTextColor = ColorOnSurface
                        )
                    )
                    ExposedDropdownMenu(
                        expanded = expandedKategori,
                        onDismissRequest = { expandedKategori = false }
                    ) {
                        kategoriOptions.forEach { option ->
                            DropdownMenuItem(
                                text = { Text(option) },
                                onClick = {
                                    kategori = option
                                    expandedKategori = false
                                }
                            )
                        }
                    }
                }
            }

            // Kondisi Barang
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Kondisi Barang", fontWeight = FontWeight.SemiBold, color = ColorOnSurface, fontSize = 14.sp)
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(kondisiOptions) { option ->
                        val isSelected = kondisi == option
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, if (isSelected) ColorPrimary else ColorOutlineVariant),
                            color = if (isSelected) ColorPrimary.copy(alpha = 0.1f) else ColorSurface,
                            modifier = Modifier.clickable { kondisi = option }
                        ) {
                            Text(
                                text = option,
                                color = if (isSelected) ColorPrimary else ColorOnSurface,
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            // Deskripsi
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text("Deskripsi", fontWeight = FontWeight.SemiBold, color = ColorOnSurface, fontSize = 14.sp)
                OutlinedTextField(
                    value = deskripsi,
                    onValueChange = { deskripsi = it },
                    placeholder = { Text("Ceritakan detail barang, alasan donasi, atau info tambahan lainnya...", color = ColorOnSurfaceVariant) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = ColorOutlineVariant,
                        focusedBorderColor = ColorPrimary,
                        unfocusedContainerColor = ColorSurface,
                        focusedContainerColor = ColorSurface
                    )
                )
            }

            // Lokasi Penjemputan
            InputField(
                label = "Lokasi Penjemputan",
                value = lokasi,
                onValueChange = { lokasi = it },
                placeholder = "Contoh: Jl. Sudirman No. 123, Jakarta Selatan"
            )

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(label, fontWeight = FontWeight.SemiBold, color = ColorOnSurface, fontSize = 14.sp)
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(placeholder, color = ColorOnSurfaceVariant) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = ColorOutlineVariant,
                focusedBorderColor = ColorPrimary,
                unfocusedContainerColor = ColorSurface,
                focusedContainerColor = ColorSurface
            )
        )
    }
}
package com.example.bagiin.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.unit.em
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bagiin.viewmodel.AuthViewModel

// Luminous Giving Colors
private val Background = Color(0xFFF8F9FF)
private val SurfaceLowest = Color(0xFFFFFFFF)
private val SurfaceContainerLow = Color(0xFFEFF4FF)
private val OnBackground = Color(0xFF0B1C30)
private val OnSurfaceVariant = Color(0xFF434655)
private val Primary = Color(0xFF004AC6)
private val OnPrimary = Color(0xFFFFFFFF)
private val OutlineVariant = Color(0xFFC3C6D7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val message = viewModel.message.value
    val loading = viewModel.loading.value

    LaunchedEffect(message) {
        if (message == "Register berhasil") {
            navController.navigate("login") {
                popUpTo("register") { inclusive = true }
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .verticalScroll(rememberScrollState()),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = SurfaceLowest),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Logo Placeholder
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = androidx.compose.ui.res.painterResource(id = com.example.bagiin.R.drawable.ic_bagiin_logo),
                        contentDescription = "Logo Bagiin",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        contentScale = androidx.compose.ui.layout.ContentScale.Fit
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Daftar Akun",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Lengkapi data diri untuk mulai berbagi kebaikan",
                    fontSize = 14.sp,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Input Fields
                val textFieldColors = TextFieldDefaults.colors(
                    focusedContainerColor = SurfaceContainerLow,
                    unfocusedContainerColor = SurfaceContainerLow,
                    disabledContainerColor = SurfaceContainerLow,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                )
                val fieldModifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
                val textFieldShape = RoundedCornerShape(12.dp)

                TextField(
                    value = nama,
                    onValueChange = { nama = it },
                    placeholder = { Text("Nama Lengkap", color = OnSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.PersonOutline, contentDescription = null, tint = OnSurfaceVariant) },
                    modifier = fieldModifier,
                    singleLine = true,
                    shape = textFieldShape,
                    colors = textFieldColors,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = OnBackground)
                )

                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("Email", color = OnSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.MailOutline, contentDescription = null, tint = OnSurfaceVariant) },
                    modifier = fieldModifier,
                    singleLine = true,
                    shape = textFieldShape,
                    colors = textFieldColors,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = OnBackground)
                )

                TextField(
                    value = noHp,
                    onValueChange = { noHp = it },
                    placeholder = { Text("Nomor HP", color = OnSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = OnSurfaceVariant) },
                    modifier = fieldModifier,
                    singleLine = true,
                    shape = textFieldShape,
                    colors = textFieldColors,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = OnBackground)
                )

                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("Password", color = OnSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = OnSurfaceVariant) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = OnSurfaceVariant
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = fieldModifier,
                    singleLine = true,
                    shape = textFieldShape,
                    colors = textFieldColors,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = OnBackground)
                )

                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text("Konfirmasi Password", color = OnSurfaceVariant) },
                    leadingIcon = { Icon(Icons.Default.Restore, contentDescription = null, tint = OnSurfaceVariant) },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = fieldModifier,
                    singleLine = true,
                    shape = textFieldShape,
                    colors = textFieldColors,
                    textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = OnBackground)
                )



                if (message.isNotEmpty() && message != "Register berhasil") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = message, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Register Button
                Button(
                    onClick = {
                        if (nama.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            viewModel.message.value = "Semua field harus diisi"
                        } else if (password != confirmPassword) {
                            viewModel.message.value = "Password tidak cocok"
                        } else if (password.length < 6) {
                            viewModel.message.value = "Password minimal 6 karakter"
                        } else {
                            viewModel.register(nama, email, password, noHp)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(9999.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    enabled = !loading
                ) {
                    if (loading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = OnPrimary)
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                "Daftar", 
                                fontSize = 14.sp, 
                                fontWeight = FontWeight.SemiBold, 
                                color = OnPrimary,
                                letterSpacing = 0.02.em
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                                contentDescription = "Daftar",
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Login link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Sudah punya akun? ", fontSize = 14.sp, color = OnSurfaceVariant)
                    TextButton(
                        onClick = { navController.navigate("login") },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "Login di sini",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = Primary
                        )
                    }
                }
            }
        }
    }
}

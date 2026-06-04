package com.example.bagiin.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
private val OnBackground = Color(0xFF0B1C30)
private val OnSurfaceVariant = Color(0xFF434655)
private val Primary = Color(0xFF004AC6)
private val OnPrimary = Color(0xFFFFFFFF)
private val OutlineVariant = Color(0xFFC3C6D7)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    val message = viewModel.message.value
    val loading = viewModel.loading.value

    LaunchedEffect(message) {
        if (message == "Login berhasil") {
            navController.navigate("dashboard") {
                popUpTo("login") { inclusive = true }
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
            modifier = Modifier.fillMaxWidth(),
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
                    text = "Selamat Datang di Bagiin",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = OnBackground,
                    textAlign = TextAlign.Center
                )
                
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Masuk untuk mulai berbagi kebaikan",
                    fontSize = 14.sp,
                    color = OnSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(32.dp))

                // Email Field
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = "Email",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = OnSurfaceVariant
                    )
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("nama@email.com", color = OnSurfaceVariant.copy(alpha = 0.7f), fontSize = 14.sp) },
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Primary,
                            unfocusedIndicatorColor = OutlineVariant,
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = OnBackground)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Password Field
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Password",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = OnSurfaceVariant
                        )
                        Text(
                            text = "Lupa Password?",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium,
                            color = Primary
                        )
                    }
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Masukkan password Anda", color = OnSurfaceVariant.copy(alpha = 0.7f), fontSize = 14.sp) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Icon(
                                    if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                    contentDescription = null,
                                    tint = OnSurfaceVariant.copy(alpha = 0.7f)
                                )
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth(),
                        singleLine = true,
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.Transparent,
                            unfocusedContainerColor = Color.Transparent,
                            disabledContainerColor = Color.Transparent,
                            focusedIndicatorColor = Primary,
                            unfocusedIndicatorColor = OutlineVariant,
                        ),
                        textStyle = LocalTextStyle.current.copy(fontSize = 14.sp, color = OnBackground)
                    )
                }

                if (message.isNotEmpty() && message != "Login berhasil") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = message, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Login Button
                Button(
                    onClick = {
                        if (email.isBlank() || password.isBlank()) {
                            viewModel.message.value = "Email dan password tidak boleh kosong"
                        } else if (password.length < 6) {
                            viewModel.message.value = "Password minimal 6 karakter"
                        } else {
                            viewModel.login(email, password)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp),
                    shape = RoundedCornerShape(9999.dp), // fully rounded pill
                    colors = ButtonDefaults.buttonColors(containerColor = Primary),
                    enabled = !loading
                ) {
                    if (loading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = OnPrimary)
                    } else {
                        Text(
                            "Login", 
                            fontSize = 14.sp, 
                            fontWeight = FontWeight.SemiBold, 
                            color = OnPrimary,
                            letterSpacing = 0.02.em
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Divider "Atau masuk dengan"
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = OutlineVariant)
                    Text(
                        text = "Atau masuk dengan",
                        modifier = Modifier.padding(horizontal = 8.dp),
                        fontSize = 12.sp,
                        color = OnSurfaceVariant
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = OutlineVariant)
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Sign up link
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Belum punya akun? ", fontSize = 14.sp, color = OnSurfaceVariant)
                    TextButton(
                        onClick = { navController.navigate("register") },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            "Daftar Akun Baru",
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

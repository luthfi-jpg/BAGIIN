package com.example.bagiin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.bagiin.ui.theme.*
import com.example.bagiin.viewmodel.AuthViewModel

@Composable
fun RegisterScreen(
    navController: NavController,
    viewModel: AuthViewModel = viewModel()
) {
    var nama by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var noHp by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
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

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "Bagiin",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = BagiinGreen
        )
        Text(
            text = "Create an account to start your giving journey.",
            fontSize = 13.sp,
            color = BagiinGreyText,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(28.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {

                // Full Name
                Text("Full Name", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = BagiinDarkText)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = nama,
                    onValueChange = { nama = it },
                    placeholder = { Text("Jane Doe", color = BagiinGreyText) },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = BagiinGreyText) },
                    modifier = Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(14.dp))

                // Email
                Text("Email Address", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = BagiinDarkText)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text("jane@example.com", color = BagiinGreyText) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = BagiinGreyText) },
                    modifier = Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(14.dp))

                // Phone
                Text("Phone Number", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = BagiinDarkText)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = noHp,
                    onValueChange = { noHp = it },
                    placeholder = { Text("+62 000-0000-0000", color = BagiinGreyText) },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = BagiinGreyText) },
                    modifier = Modifier.fillMaxWidth(),
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

                Spacer(modifier = Modifier.height(14.dp))

                // Password
                Text("Password", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = BagiinDarkText)
                Spacer(modifier = Modifier.height(6.dp))
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text("••••••••", color = BagiinGreyText) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = BagiinGreyText) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = null,
                                tint = BagiinGreyText
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
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
                Text(
                    "Must be at least 6 characters long.",
                    fontSize = 11.sp,
                    color = BagiinGreyText,
                    modifier = Modifier.padding(top = 4.dp)
                )

                if (message.isNotEmpty() && message != "Register berhasil") {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = message, color = MaterialTheme.colorScheme.error, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Button(
                    onClick = {
                        if (nama.isBlank() || email.isBlank() || password.isBlank()) {
                            viewModel.message.value = "Semua field harus diisi"
                        } else if (password.length < 6) {
                            viewModel.message.value = "Password minimal 6 karakter"
                        } else {
                            viewModel.register(nama, email, password, noHp)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = BagiinGreen),
                    enabled = !loading
                ) {
                    if (loading) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), color = Color.White)
                    } else {
                        Text("Sign Up →", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Already have an account? ", fontSize = 14.sp, color = BagiinGreyText)
            TextButton(
                onClick = { navController.navigate("login") },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text(
                    "Sign In",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = BagiinGreen
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}
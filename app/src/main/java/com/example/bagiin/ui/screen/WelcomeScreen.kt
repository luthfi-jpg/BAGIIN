package com.example.bagiin.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

val BagiinGreen = Color(0xFF1B5E35)
val BagiinGreenLight = Color(0xFFE8F5E9)
val BagiinGreenMid = Color(0xFF2E7D52)
val BagiinGrey = Color(0xFFF5F5F5)
val BagiinGreyText = Color(0xFF9E9E9E)
val BagiinDarkText = Color(0xFF1A1A1A)

@Composable
fun WelcomeScreen(navController: NavController) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        // Logo
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                shape = CircleShape,
                color = BagiinGreen,
                modifier = Modifier.size(32.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text("🤲", fontSize = 16.sp)
                }
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Bagiin",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = BagiinGreen
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        // Illustration placeholder
        Surface(
            modifier = Modifier.size(220.dp),
            shape = CircleShape,
            color = BagiinGreenLight
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text("🤝", fontSize = 80.sp)
            }
        }

        Spacer(modifier = Modifier.height(40.dp))

        Text(
            text = "Give what you can,\ntake what you need",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = BagiinDarkText,
            textAlign = TextAlign.Center,
            lineHeight = 34.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Join a community dedicated to reducing waste and helping neighbors through simple, noble acts of giving.",
            fontSize = 14.sp,
            color = BagiinGreyText,
            textAlign = TextAlign.Center,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = { navController.navigate("register") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = BagiinGreen)
        ) {
            Text(
                "Get Started",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = { navController.navigate("login") },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(contentColor = BagiinGreen)
        ) {
            Text(
                "Log In",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
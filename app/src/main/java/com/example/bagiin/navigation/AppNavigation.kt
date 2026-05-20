package com.example.bagiin.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.ui.screen.*
import io.github.jan.supabase.auth.auth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val session = SupabaseInstance.client.auth.currentSessionOrNull()
    val startDestination = if (session != null) "dashboard" else "welcome"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = "welcome") {
            WelcomeScreen(navController = navController)
        }
        composable(route = "login") {
            LoginScreen(navController = navController)
        }
        composable(route = "register") {
            RegisterScreen(navController = navController)
        }
        composable(route = "dashboard") {
            DashboardScreen(navController = navController)
        }
        composable(route = "profile") {
            ProfileScreen(navController = navController)
        }
    }
}
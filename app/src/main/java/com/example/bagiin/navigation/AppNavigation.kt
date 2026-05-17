package com.example.bagiin.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.bagiin.ui.screen.DashboardScreen
import com.example.bagiin.ui.screen.LoginScreen
import com.example.bagiin.ui.screen.ProfileScreen
import com.example.bagiin.ui.screen.RegisterScreen
import com.example.bagiin.ui.screen.WelcomeScreen

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "welcome"
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

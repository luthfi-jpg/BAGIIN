package com.example.bagiin.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bagiin.data.SupabaseInstance
import com.example.bagiin.ui.screen.*
import io.github.jan.supabase.auth.auth

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val session = SupabaseInstance.client.auth.currentSessionOrNull()
    val startDestination = if (session != null) "dashboard" else "login"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(route = "onboarding_donate") {
            OnboardingDonateScreen(
                onSkipClick = { navController.navigate("login") },
                onNextClick = { navController.navigate("login") }
            )
        }
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
        composable(route = "upload_donasi") {
            UploadDonasiScreen(navController = navController)
        }
        composable(route = "daftar_barang") {
            DaftarBarangScreen(navController = navController)
        }

        composable(route = "riwayat_donasi") {
            RiwayatDonasiScreen(navController = navController)
        }

        // Detail Barang screen with item title argument
        composable(
            route = "detail_barang/{itemTitle}",
            arguments = listOf(navArgument("itemTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemTitle = backStackEntry.arguments?.getString("itemTitle") ?: "Unknown Item"
            DetailBarangScreen(navController = navController, itemTitle = itemTitle)
        }

        // Klaim Barang screen with item ID and title argument
        composable(
            route = "klaim_barang/{idDonasi}/{itemTitle}",
            arguments = listOf(
                navArgument("idDonasi") { type = NavType.StringType },
                navArgument("itemTitle") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val idDonasi = backStackEntry.arguments?.getString("idDonasi") ?: ""
            val itemTitle = backStackEntry.arguments?.getString("itemTitle") ?: "Unknown Item"
            KlaimBarangScreen(navController = navController, idDonasi = idDonasi, itemTitle = itemTitle)
        }

        // Jadwal Penyerahan screen with item title argument
        composable(
            route = "jadwal_penyerahan/{itemTitle}",
            arguments = listOf(navArgument("itemTitle") { type = NavType.StringType })
        ) { backStackEntry ->
            val itemTitle = backStackEntry.arguments?.getString("itemTitle") ?: "Unknown Item"
            JadwalPenyerahanScreen(navController = navController, itemTitle = itemTitle)
        }
    }
}

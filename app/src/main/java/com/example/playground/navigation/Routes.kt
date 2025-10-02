package com.example.playground.navigation

// --- Routes sealed class for type-safety and centralization of route names ---
sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Main : Screen("main")







    // Bottom Nav destinations (within Main)
    object BottomHome : Screen("bottom_home")
    object BottomProfile : Screen("bottom_profile")
    // Drawer extra route example
    object Settings : Screen("settings")
}

//navController.navigate("settings")

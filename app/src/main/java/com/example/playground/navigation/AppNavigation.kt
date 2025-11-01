package com.example.playground.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.playground.ui.features.auth.signup.view.RegisterScreen2
import com.example.playground.ui.features.auth.login.view.LoginScreen

// --- Navigation Graph ---
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    navController: NavHostController
) {


        NavHost(
            modifier = Modifier,
            navController = navController,
            startDestination = Screen.Signup.route
        ) {
            // Login screen composable
            composable(Screen.Login.route) {
                LoginScreen(
                    onLoginSuccess = {
                        // Navigate to main. Clear the login from back stack so Back won't return to it.
                        navController.navigate(Screen.Main.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    },
                    navController = navController
                )
            }

            // Main route hosts the drawer + bottom nav content
            composable(Screen.Main.route) {
                // Show the MainLayout composable that contains drawer and bottom nav.
                MainLayout(
                    topLevelNavController = navController // pass top-level to allow logout
                )
            }


            composable(Screen.Settings.route) {
                Scaffold(
                    topBar = { TopAppBar(title = { Text("Settings") }) }
                ) { innerPadding ->
                    Text(
                        text = "Settings content goes here",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }

            composable(Screen.Signup.route) {
                RegisterScreen2(
                    navController = navController
                )
            }









        }

}

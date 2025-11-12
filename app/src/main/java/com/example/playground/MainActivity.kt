package com.example.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.playground.navigation.AppNavigation
import com.example.playground.ui.features.sharedPreference.themePrefs.viewModel.ThemeViewModel
import com.example.playground.ui.theme.PlayGroundTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            // Top-level navController (controls Login <-> Main)
            val navController = rememberNavController()

            val viewModel: ThemeViewModel = viewModel()

            val isDark by viewModel.isDark.collectAsState(initial = false)
            PlayGroundTheme (
                darkTheme = isDark
            ){
                // AppNavigation hosts the top-level graph
                AppNavigation(navController = navController)
            }
        }
    }
}

package com.example.playground

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.navigation.compose.rememberNavController
import com.example.playground.navigation.AppNavigation
import com.example.playground.ui.theme.PlayGroundTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            // Top-level navController (controls Login <-> Main)
            val navController = rememberNavController()

            PlayGroundTheme {
                // AppNavigation hosts the top-level graph
                AppNavigation(navController = navController)
            }
        }
    }
}

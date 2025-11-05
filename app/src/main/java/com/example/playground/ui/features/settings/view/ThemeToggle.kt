package com.example.playground.ui.features.settings.view

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.playground.ui.features.settings.viewModel.ThemeViewModel

@Composable
fun ThemeToggle(
    viewModel: ThemeViewModel
) {
    val isDark by viewModel.isDark.collectAsState(initial = false)

    Switch(
        checked = isDark,
        onCheckedChange = { viewModel.toggleTheme(it) }
    )
}

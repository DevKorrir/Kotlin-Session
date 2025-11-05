package com.example.playground.ui.features.settings.view

import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.playground.ui.features.sharedPreference.themePrefs.viewModel.ThemeViewModel

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

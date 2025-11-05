package com.example.playground.ui.features.settings.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playground.ui.features.sharedPreference.themePrefs.repo.ThemePreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    private val prefs: ThemePreference
): ViewModel() {
    val isDark = prefs.isDarkMode.stateIn(viewModelScope, SharingStarted.Eagerly, false)
    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch { prefs.saveTheme(isDark) }
    }
}

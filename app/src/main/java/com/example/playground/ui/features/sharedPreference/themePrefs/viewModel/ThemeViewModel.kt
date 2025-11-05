package com.example.playground.ui.features.sharedPreference.themePrefs.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playground.ui.features.sharedPreference.themePrefs.repo.ThemePreference
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ThemeViewModel(
    application: Application
) : AndroidViewModel(application) {
    private val prefs = ThemePreference(application)
    val isDark = prefs.isDarkMode.stateIn(viewModelScope, SharingStarted.Companion.Eagerly, false)
    fun toggleTheme(isDark: Boolean) {
        viewModelScope.launch { prefs.saveTheme(isDark) }
    }
}
package com.example.playground.ui.features.sharedPreference.themePrefs.repo

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore("settings")
val THEME_KEY = booleanPreferencesKey("dark_mode")


class ThemePreference(private val context: Context) {
    val isDarkMode = context.dataStore.data.map {
        it[THEME_KEY] ?: false
    }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[THEME_KEY] = isDark
        }
    }
}

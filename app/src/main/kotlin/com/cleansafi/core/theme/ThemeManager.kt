package com.cleansafi.core.theme

import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ThemeManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val prefs = context.getSharedPreferences("theme_prefs", Context.MODE_PRIVATE)
    
    var isDarkTheme by mutableStateOf(prefs.getBoolean("is_dark_theme", false))
        private set
    
    fun toggleTheme() {
        isDarkTheme = !isDarkTheme
        prefs.edit().putBoolean("is_dark_theme", isDarkTheme).apply()
    }
    
    fun setTheme(isDark: Boolean) {
        isDarkTheme = isDark
        prefs.edit().putBoolean("is_dark_theme", isDark).apply()
    }
}

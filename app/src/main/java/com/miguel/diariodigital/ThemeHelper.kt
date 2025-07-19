package com.miguel.diariodigital

import android.content.Context
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private const val PREF_NAME = "user_preferences"
private val Context.dataStore by preferencesDataStore(name = PREF_NAME)

object ThemeHelper {
    private const val PREFS_NAME = "user_prefs"
    private const val DARK_MODE_KEY = "dark_theme"

    suspend fun getThemePreference(context: Context): kotlinx.coroutines.flow.Flow<Boolean> {
        val dataStore = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val isDark = dataStore.getBoolean(DARK_MODE_KEY, false)
        return kotlinx.coroutines.flow.flowOf(isDark)
    }

    fun saveThemePreference(context: Context, isDark: Boolean) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        prefs.edit().putBoolean(DARK_MODE_KEY, isDark).apply()
    }

    fun applyTheme(isDarkMode: Boolean) {
        AppCompatDelegate.setDefaultNightMode(
            if (isDarkMode) AppCompatDelegate.MODE_NIGHT_YES
            else AppCompatDelegate.MODE_NIGHT_NO
        )
    }
}

package com.parvez.calculator.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.calculatorDataStore by preferencesDataStore(name = "calculator_preferences")

class CalculatorPreferences(private val context: Context) {
    private val darkTheme = booleanPreferencesKey("dark_theme")
    private val angleMode = stringPreferencesKey("angle_mode")
    private val precision = stringPreferencesKey("precision")

    val darkThemeFlow: Flow<Boolean> = context.calculatorDataStore.data.map { it[darkTheme] ?: false }
    val angleModeFlow: Flow<String> = context.calculatorDataStore.data.map { it[angleMode] ?: "DEGREES" }
    val precisionFlow: Flow<String> = context.calculatorDataStore.data.map { it[precision] ?: "AUTO" }

    suspend fun setDarkTheme(enabled: Boolean) {
        context.calculatorDataStore.edit { it[darkTheme] = enabled }
    }
    suspend fun setAngleMode(mode: String) {
        require(mode in setOf("DEGREES", "RADIANS", "GRADIANS"))
        context.calculatorDataStore.edit { it[angleMode] = mode }
    }
    suspend fun setPrecision(value: String) {
        context.calculatorDataStore.edit { it[precision] = value }
    }
}

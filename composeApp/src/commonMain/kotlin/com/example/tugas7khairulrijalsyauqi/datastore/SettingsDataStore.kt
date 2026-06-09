package com.example.tugas7khairulrijalsyauqi.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.example.tugas7khairulrijalsyauqi.model.SortOrder
import com.example.tugas7khairulrijalsyauqi.model.UserSettings
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SettingsDataStore(private val dataStore: DataStore<Preferences>) {

    private object PreferencesKeys {
        val IS_DARK_THEME = booleanPreferencesKey("is_dark_theme")
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    val settingsFlow: Flow<UserSettings> = dataStore.data.map { preferences ->
        val isDarkTheme = preferences[PreferencesKeys.IS_DARK_THEME] ?: false
        val sortOrderString = preferences[PreferencesKeys.SORT_ORDER] ?: SortOrder.NEWEST_FIRST.name
        val sortOrder = try {
            SortOrder.valueOf(sortOrderString)
        } catch (e: IllegalArgumentException) {
            SortOrder.NEWEST_FIRST
        }
        UserSettings(isDarkTheme, sortOrder)
    }

    suspend fun updateDarkTheme(isDarkTheme: Boolean) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_DARK_THEME] = isDarkTheme
        }
    }

    suspend fun updateSortOrder(sortOrder: SortOrder) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.SORT_ORDER] = sortOrder.name
        }
    }
}
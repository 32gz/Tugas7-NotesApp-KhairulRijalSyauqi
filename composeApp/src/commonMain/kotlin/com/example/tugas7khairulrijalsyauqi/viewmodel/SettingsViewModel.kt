package com.example.tugas7khairulrijalsyauqi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tugas7khairulrijalsyauqi.datastore.SettingsDataStore
import com.example.tugas7khairulrijalsyauqi.model.SortOrder
import com.example.tugas7khairulrijalsyauqi.model.UserSettings
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(private val settingsDataStore: SettingsDataStore) : ViewModel() {

    val settings: StateFlow<UserSettings> = settingsDataStore.settingsFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserSettings()
        )

    fun toggleDarkTheme() {
        viewModelScope.launch {
            settingsDataStore.updateDarkTheme(!settings.value.isDarkTheme)
        }
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        viewModelScope.launch {
            settingsDataStore.updateSortOrder(sortOrder)
        }
    }

    class Factory(private val settingsDataStore: SettingsDataStore) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return SettingsViewModel(settingsDataStore) as T
        }
    }
}
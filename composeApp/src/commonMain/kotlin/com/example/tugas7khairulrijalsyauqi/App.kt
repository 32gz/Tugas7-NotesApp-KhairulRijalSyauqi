package com.example.tugas7khairulrijalsyauqi

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import com.example.tugas7khairulrijalsyauqi.datastore.SettingsDataStore
import com.example.tugas7khairulrijalsyauqi.model.UserSettings
import com.example.tugas7khairulrijalsyauqi.navigation.NotesNavHost
import com.example.tugas7khairulrijalsyauqi.repository.NotesRepository
import com.example.tugas7khairulrijalsyauqi.ui.theme.NotesAppTheme
import com.example.tugas7khairulrijalsyauqi.viewmodel.NotesUiState
import kotlinx.coroutines.launch

@Composable
fun App(
    repository: NotesRepository,
    settingsDataStore: SettingsDataStore
) {
    val settings by settingsDataStore.settingsFlow.collectAsState(initial = UserSettings())
    val notesViewModel = remember { com.example.tugas7khairulrijalsyauqi.viewmodel.NotesViewModel(repository) }
    val notesUiState by notesViewModel.uiState.collectAsState()
    val searchQuery by notesViewModel.searchQuery.collectAsState()
    val sortOrder by notesViewModel.sortOrder.collectAsState()

    val favoriteNotes by notesViewModel.favoriteNotes.collectAsState()
    val scope = rememberCoroutineScope()

    NotesAppTheme(darkTheme = settings.isDarkTheme) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            NotesNavHost(
                notesUiState = notesUiState,
                favoriteNotes = favoriteNotes,
                searchQuery = searchQuery,
                sortOrder = sortOrder,
                settings = settings,
                repository = repository,
                onSearch = { notesViewModel.search(it) },
                onToggleFavorite = { id, isFavorite ->
                    notesViewModel.toggleFavorite(id, isFavorite)
                },
                onDeleteNote = { notesViewModel.deleteNote(it) },
                onToggleDarkTheme = {
                    scope.launch {
                        settingsDataStore.updateDarkTheme(!settings.isDarkTheme)
                    }
                },
                onSortOrderChange = { notesViewModel.updateSortOrder(it) },
                onRefreshNotes = { notesViewModel.refreshNotes() }
            )
        }
    }
}
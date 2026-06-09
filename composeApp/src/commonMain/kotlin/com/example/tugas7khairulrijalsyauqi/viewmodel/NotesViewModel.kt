package com.example.tugas7khairulrijalsyauqi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tugas7khairulrijalsyauqi.model.Note
import com.example.tugas7khairulrijalsyauqi.model.SortOrder
import com.example.tugas7khairulrijalsyauqi.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotesViewModel(private val repository: NotesRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _sortOrder = MutableStateFlow(SortOrder.NEWEST_FIRST)
    val sortOrder: StateFlow<SortOrder> = _sortOrder.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _favoriteNotes = MutableStateFlow<List<Note>>(emptyList())
    val favoriteNotes: StateFlow<List<Note>> = _favoriteNotes.asStateFlow()

    init {
        loadNotes()
        loadFavoriteNotes()
    }

    private fun loadFavoriteNotes() {
        viewModelScope.launch {
            repository.getFavoriteNotes().collectLatest { notes ->
                _favoriteNotes.value = notes
            }
        }
    }

    fun loadNotes(sortOrder: SortOrder = _sortOrder.value) {
        viewModelScope.launch {
            _uiState.value = NotesUiState.Loading
            val query = _searchQuery.value

            val flow = if (query.isBlank()) {
                repository.getAllNotes(sortOrder)
            } else {
                repository.searchNotes(query, sortOrder)
            }

            flow.catch { e ->
                _uiState.value = NotesUiState.Empty
            }.collectLatest { notes ->
                _uiState.value = if (notes.isEmpty()) {
                    NotesUiState.Empty
                } else {
                    NotesUiState.Content(
                        notes = notes,
                        searchQuery = query,
                        sortOrder = sortOrder
                    )
                }
            }
        }
    }

    fun refreshNotes() {
        loadNotes()
        loadFavoriteNotes()
    }

    fun search(query: String) {
        _searchQuery.value = query
        loadNotes()
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        _sortOrder.value = sortOrder
        loadNotes(sortOrder)
    }

    fun toggleFavorite(noteId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            repository.toggleFavorite(noteId, isFavorite)
            loadNotes()
            loadFavoriteNotes()
        }
    }

    fun deleteNote(noteId: Long) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
            loadNotes()
            loadFavoriteNotes()
        }
    }

    class Factory(private val repository: NotesRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return NotesViewModel(repository) as T
        }
    }
}
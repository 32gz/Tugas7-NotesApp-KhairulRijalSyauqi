package com.example.tugas7khairulrijalsyauqi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tugas7khairulrijalsyauqi.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NoteDetailViewModel(
    private val repository: NotesRepository,
    private val noteId: Long
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteDetailUiState())
    val uiState: StateFlow<NoteDetailUiState> = _uiState.asStateFlow()

    init {
        loadNote()
    }

    private fun loadNote() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            repository.getNoteById(noteId)
                .catch { _uiState.value = NoteDetailUiState(isLoading = false) }
                .collectLatest { note ->
                    _uiState.value = NoteDetailUiState(note = note, isLoading = false)
                }
        }
    }

    fun toggleFavorite() {
        val note = _uiState.value.note ?: return
        viewModelScope.launch {
            repository.toggleFavorite(note.id, !note.isFavorite)
        }
    }

    fun deleteNote(onDeleted: () -> Unit) {
        viewModelScope.launch {
            repository.deleteNote(noteId)
            onDeleted()
        }
    }

    class Factory(
        private val repository: NotesRepository,
        private val noteId: Long
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return NoteDetailViewModel(repository, noteId) as T
        }
    }
}
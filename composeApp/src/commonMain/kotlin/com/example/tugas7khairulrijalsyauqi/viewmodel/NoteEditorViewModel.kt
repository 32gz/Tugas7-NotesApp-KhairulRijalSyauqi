package com.example.tugas7khairulrijalsyauqi.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.tugas7khairulrijalsyauqi.repository.NotesRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class NoteEditorViewModel(
    private val repository: NotesRepository,
    private val noteId: Long? = null
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteEditorUiState(id = noteId))
    val uiState: StateFlow<NoteEditorUiState> = _uiState.asStateFlow()

    init {
        if (noteId != null) {
            loadNote()
        }
    }

    private fun loadNote() {
        viewModelScope.launch {
            repository.getNoteById(noteId!!).first()?.let { note ->
                _uiState.value = _uiState.value.copy(
                    title = note.title,
                    content = note.content
                )
            }
        }
    }

    fun updateTitle(title: String) {
        _uiState.value = _uiState.value.copy(
            title = title,
            titleError = false
        )
    }

    fun updateContent(content: String) {
        _uiState.value = _uiState.value.copy(
            content = content,
            contentError = false
        )
    }

    fun save(onSuccess: () -> Unit) {
        val state = _uiState.value

        if (state.title.isBlank()) {
            _uiState.value = state.copy(titleError = true)
            return
        }

        if (state.content.isBlank()) {
            _uiState.value = state.copy(contentError = true)
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isSaving = true)

            if (state.id != null) {
                repository.updateNote(state.id, state.title, state.content)
            } else {
                repository.insertNote(state.title, state.content)
            }

            onSuccess()
        }
    }

    class Factory(
        private val repository: NotesRepository,
        private val noteId: Long? = null
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            @Suppress("UNCHECKED_CAST")
            return NoteEditorViewModel(repository, noteId) as T
        }
    }
}
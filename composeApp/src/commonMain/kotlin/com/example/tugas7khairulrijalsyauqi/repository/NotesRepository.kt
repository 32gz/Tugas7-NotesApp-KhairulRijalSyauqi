package com.example.tugas7khairulrijalsyauqi.repository

import com.example.tugas7khairulrijalsyauqi.NotesDatabaseQueries
import com.example.tugas7khairulrijalsyauqi.model.Note
import com.example.tugas7khairulrijalsyauqi.model.SortOrder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext

class NotesRepository(private val queries: NotesDatabaseQueries) {

    fun getAllNotes(sortOrder: SortOrder = SortOrder.NEWEST_FIRST): Flow<List<Note>> = flow {
        val notesList = when (sortOrder) {
            SortOrder.NEWEST_FIRST -> queries.getAllNotesNewest().executeAsList()
            SortOrder.OLDEST_FIRST -> queries.getAllNotesOldest().executeAsList()
            SortOrder.ALPHABETICAL -> queries.getAllNotesAlphabetical().executeAsList()
            SortOrder.LAST_MODIFIED -> queries.getAllNotesLastModified().executeAsList()
        }
        emit(notesList.map { note ->
            Note(
                id = note.id,
                title = note.title,
                content = note.content,
                isFavorite = note.is_favorite == 1L,
                createdAt = note.created_at,
                updatedAt = note.updated_at
            )
        })
    }.flowOn(Dispatchers.IO)

    fun getFavoriteNotes(): Flow<List<Note>> = flow {
        val notesList = queries.getFavoriteNotes().executeAsList()
        emit(notesList.map { note ->
            Note(
                id = note.id,
                title = note.title,
                content = note.content,
                isFavorite = note.is_favorite == 1L,
                createdAt = note.created_at,
                updatedAt = note.updated_at
            )
        })
    }.flowOn(Dispatchers.IO)

    fun searchNotes(query: String, sortOrder: SortOrder = SortOrder.NEWEST_FIRST): Flow<List<Note>> = flow {
        val notesList = when (sortOrder) {
            SortOrder.NEWEST_FIRST -> queries.searchNotesNewest("%$query%").executeAsList()
            SortOrder.OLDEST_FIRST -> queries.searchNotesOldest("%$query%").executeAsList()
            SortOrder.ALPHABETICAL -> queries.searchNotesAlphabetical("%$query%").executeAsList()
            SortOrder.LAST_MODIFIED -> queries.searchNotesLastModified("%$query%").executeAsList()
        }
        emit(notesList.map { note ->
            Note(
                id = note.id,
                title = note.title,
                content = note.content,
                isFavorite = note.is_favorite == 1L,
                createdAt = note.created_at,
                updatedAt = note.updated_at
            )
        })
    }.flowOn(Dispatchers.IO)

    fun getNoteById(id: Long): Flow<Note?> = flow {
        val note = queries.getNoteById(id).executeAsOneOrNull()
        emit(note?.let {
            Note(
                id = it.id,
                title = it.title,
                content = it.content,
                isFavorite = it.is_favorite == 1L,
                createdAt = it.created_at,
                updatedAt = it.updated_at
            )
        })
    }.flowOn(Dispatchers.IO)

    suspend fun insertNote(title: String, content: String): Long {
        return withContext(Dispatchers.IO) {
            val now = System.currentTimeMillis()
            queries.insertNote(title, content, 0, now, now)
            1L
        }
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        withContext(Dispatchers.IO) {
            queries.updateNote(title, content, System.currentTimeMillis(), id)
        }
    }

    suspend fun toggleFavorite(id: Long, isFavorite: Boolean) {
        withContext(Dispatchers.IO) {
            queries.updateFavorite(if (isFavorite) 1L else 0L, System.currentTimeMillis(), id)
        }
    }

    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteNote(id)
        }
    }
}
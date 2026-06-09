package com.example.tugas7khairulrijalsyauqi.model

data class Note(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)

enum class SortOrder {
    NEWEST_FIRST,
    OLDEST_FIRST,
    ALPHABETICAL,
    LAST_MODIFIED
}

data class UserSettings(
    val isDarkTheme: Boolean = false,
    val sortOrder: SortOrder = SortOrder.NEWEST_FIRST
)
package com.example.tugas7khairulrijalsyauqi.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.tugas7khairulrijalsyauqi.model.Note
import com.example.tugas7khairulrijalsyauqi.ui.components.EmptyContent
import com.example.tugas7khairulrijalsyauqi.ui.components.NoteCard

@Composable
fun FavoritesScreen(
    notes: List<Note>,
    onNoteClick: (Long) -> Unit,
    onFavoriteToggle: (Long, Boolean) -> Unit,
    onDelete: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Favorit",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "${notes.size} catatan favorit",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (notes.isEmpty()) {
            EmptyContent(
                title = "Tidak ada catatan favorit",
                subtitle = "Tekan ❤️ pada catatan untuk menambahkan",
                emoji = "❤️"
            )
        } else {
            LazyColumn {
                items(notes, key = { it.id }) { note ->
                    NoteCard(
                        note = note,
                        onClick = { onNoteClick(note.id) },
                        onFavoriteToggle = { onFavoriteToggle(note.id, !note.isFavorite) },
                        onDelete = { onDelete(note.id) }
                    )
                }
            }
        }
    }
}
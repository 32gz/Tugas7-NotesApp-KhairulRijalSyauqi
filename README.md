* Nama : Khairul Rijal Syauqi
* NIM  : 123140143

Aplikasi catatan modern dengan fitur CRUD lengkap, pencarian, pengaturan, dan mode offline. Dibangun menggunakan Kotlin Multiplatform dengan Jetpack Compose untuk UI dan SQLDelight untuk database lokal.

## Fitur Utama

| Fitur | Deskripsi |
|-------|-----------|
| **CRUD Notes** | Create, Read, Update, Delete catatan |
| **Search** | Pencarian catatan berdasarkan judul dan konten |
| **Favorites** | Tandai catatan sebagai favorit |
| **Dark Mode** | Toggle tema gelap/terang |
| **Sort Order** | Urutkan catatan (Terbaru, Terlama, A-Z, Terakhir Diubah) |
| **Offline Mode** | Semua data tersimpan lokal dengan SQLDelight |
| **DataStore** | Pengaturan pengguna persistensi dengan Preferences DataStore |

## Screenshots

| Screen | Description |
|--------|-------------|
| **Home/Notes List** | Menampilkan semua catatan dengan search bar |
| **Add Note** | Form untuk membuat catatan baru |
| **Edit Note** | Form untuk mengedit catatan yang ada |
| **Note Detail** | Detail lengkap catatan |
| **Favorites** | Daftar catatan favorit |
| **Settings** | Pengaturan aplikasi |
| **Profile** | Profil pengguna |

### Screenshot Preview

* Home/Notes List

* Add Note

* Edit Note

* Note Detail

* Favorites

* Settings

* Profile



## Video Demo

Video demo (~45 detik) yang menunjukkan:
- CRUD Operations (Create, Read, Update, Delete)
- Search functionality
- Settings dan Dark Mode toggle
- Offline mode demonstration

**[Watch Demo Video](assets/demo/notes-app-demo.mp4)**

## Database Schema

### SQLDelight Database: `notes.db`

```sql
-- Notes Table
CREATE TABLE notes (
    id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    title TEXT NOT NULL,
    content TEXT NOT NULL,
    is_favorite INTEGER NOT NULL DEFAULT 0,
    created_at INTEGER NOT NULL,
    updated_at INTEGER NOT NULL
);

-- Indexes for optimized queries
CREATE INDEX notes_favorite_idx ON notes(is_favorite);
CREATE INDEX notes_created_idx ON notes(created_at DESC);
CREATE INDEX notes_updated_idx ON notes(updated_at DESC);

-- Queries for different sort orders
getAllNotesNewest:
SELECT * FROM notes ORDER BY created_at DESC;

getAllNotesOldest:
SELECT * FROM notes ORDER BY created_at ASC;

getAllNotesAlphabetical:
SELECT * FROM notes ORDER BY title ASC;

getAllNotesLastModified:
SELECT * FROM notes ORDER BY updated_at DESC;

getFavoriteNotes:
SELECT * FROM notes WHERE is_favorite = 1 ORDER BY created_at DESC;

searchNotesNewest:
SELECT * FROM notes WHERE title LIKE :query OR content LIKE :query ORDER BY created_at DESC;

searchNotesOldest:
SELECT * FROM notes WHERE title LIKE :query OR content LIKE :query ORDER BY created_at ASC;

searchNotesAlphabetical:
SELECT * FROM notes WHERE title LIKE :query OR content LIKE :query ORDER BY title ASC;

searchNotesLastModified:
SELECT * FROM notes WHERE title LIKE :query OR content LIKE :query ORDER BY updated_at DESC;

getNoteById:
SELECT * FROM notes WHERE id = :id;

insertNote:
INSERT INTO notes (title, content, is_favorite, created_at, updated_at)
VALUES (:title, :content, :isFavorite, :createdAt, :updatedAt);

updateNote:
UPDATE notes SET title = :title, content = :content, updated_at = :updatedAt WHERE id = :id;

updateFavorite:
UPDATE notes SET is_favorite = :isFavorite, updated_at = :updatedAt WHERE id = :id;

deleteNote:
DELETE FROM notes WHERE id = :id;
```

### Entity Model

```kotlin
data class Note(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val isFavorite: Boolean = false,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
```

### User Settings (DataStore)

```kotlin
data class UserSettings(
    val isDarkTheme: Boolean = false,
    val sortOrder: SortOrder = SortOrder.NEWEST_FIRST
)

enum class SortOrder {
    NEWEST_FIRST,    // Urutkan dari terbaru
    OLDEST_FIRST,    // Urutkan dari terlama
    ALPHABETICAL,    // Urutkan A-Z
    LAST_MODIFIED   // Urutkan berdasarkan perubahan terakhir
}
```

## 📁 Project Structure

```
Tugas7KhairulRijalSyauqi3/
├── composeApp/
│   ├── src/
│   │   ├── androidMain/
│   │   │   └── kotlin/
│   │   │       └── com/example/tugas7khairulrijalsyauqi/
│   │   │           ├── database/
│   │   │           │   └── DatabaseDriverFactory.kt
│   │   │           ├── MainActivity.kt
│   │   │           └── Platform.android.kt
│   │   ├── commonMain/
│   │   │   └── kotlin/
│   │   │       └── com/example/tugas7khairulrijalsyauqi/
│   │   │           ├── App.kt                    # Main App Composable
│   │   │           ├── model/
│   │   │           │   └── Note.kt              # Data models
│   │   │           ├── repository/
│   │   │           │   └── NotesRepository.kt   # Database operations
│   │   │           ├── datastore/
│   │   │           │   └── SettingsDataStore.kt # Preferences storage
│   │   │           ├── screens/
│   │   │           │   ├── NotesListScreen.kt
│   │   │           │   ├── AddNoteScreen.kt
│   │   │           │   ├── EditNoteScreen.kt
│   │   │           │   ├── NoteDetailScreen.kt
│   │   │           │   ├── FavoritesScreen.kt
│   │   │           │   ├── SettingsScreen.kt
│   │   │           │   └── ProfileScreen.kt
│   │   │           ├── navigation/
│   │   │           │   ├── DrawerNavigation.kt
│   │   │           │   └── NotesNavHost.kt
│   │   │           ├── viewmodel/
│   │   │           │   ├── NotesViewModel.kt
│   │   │           │   ├── NoteDetailViewModel.kt
│   │   │           │   ├── NoteEditorViewModel.kt
│   │   │           │   ├── SettingsViewModel.kt
│   │   │           │   └── ProfileViewModel.kt
│   │   │           └── ui/
│   │   │               ├── components/
│   │   │               │   ├── NoteCard.kt
│   │   │               │   ├── SearchBar.kt
│   │   │               │   └── UiStates.kt
│   │   │               └── theme/
│   │   │                   └── Theme.kt
│   │   └── iosMain/
│   └── build.gradle.kts
├── shared/
│   └── src/
│       └── commonMain/
│           └── kotlin/
│               └── shared/
│                   └── Greeting.kt
└── iosApp/
```

## Teknologi yang Digunakan

| Teknologi | Deskripsi |
|-----------|-----------|
| **Kotlin Multiplatform** | Kode bersama untuk Android & iOS |
| **Jetpack Compose** | UI toolkit untuk Native look |
| **SQLDelight** | Database dengan type-safe SQL |
| **DataStore** | Preferences storage |
| **Kotlin Coroutines & Flow** | Async operations & reactive streams |
| **Material 3** | Design system |

## Cara Menjalankan

### Android

```bash
# Build debug APK
./gradlew :composeApp:assembleDebug

# Install ke emulator/device
./gradlew :composeApp:installDebug
```


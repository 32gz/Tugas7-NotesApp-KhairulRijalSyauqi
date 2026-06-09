package com.example.tugas7khairulrijalsyauqi.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.tugas7khairulrijalsyauqi.model.SortOrder
import com.example.tugas7khairulrijalsyauqi.model.UserSettings
import com.example.tugas7khairulrijalsyauqi.repository.NotesRepository
import com.example.tugas7khairulrijalsyauqi.screens.AddNoteScreen
import com.example.tugas7khairulrijalsyauqi.screens.EditNoteScreen
import com.example.tugas7khairulrijalsyauqi.screens.FavoritesScreen
import com.example.tugas7khairulrijalsyauqi.screens.NoteDetailScreen
import com.example.tugas7khairulrijalsyauqi.screens.NotesListScreen
import com.example.tugas7khairulrijalsyauqi.screens.ProfileScreen
import com.example.tugas7khairulrijalsyauqi.screens.SettingsScreen
import com.example.tugas7khairulrijalsyauqi.viewmodel.NoteDetailUiState
import com.example.tugas7khairulrijalsyauqi.viewmodel.NoteEditorUiState
import com.example.tugas7khairulrijalsyauqi.viewmodel.NotesUiState
import kotlinx.coroutines.launch

sealed class BottomNavItem(
    val route: String,
    val icon: ImageVector,
    val label: String
) {
    object Notes : BottomNavItem("notes_tab", Icons.AutoMirrored.Filled.List, "Notes")
    object Favorites : BottomNavItem("favorites_tab", Icons.Default.Favorite, "Favorites")
    object Profile : BottomNavItem("profile_tab", Icons.Default.Person, "Profile")
}

sealed class Screen(val route: String) {
    object NoteDetail : Screen("note_detail/{noteId}") {
        fun createRoute(noteId: Long) = "note_detail/$noteId"
    }
    object AddNote : Screen("add_note")
    object EditNote : Screen("edit_note/{noteId}") {
        fun createRoute(noteId: Long) = "edit_note/$noteId"
    }
    object Settings : Screen("settings")
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesNavHost(
    notesUiState: NotesUiState,
    favoriteNotes: List<com.example.tugas7khairulrijalsyauqi.model.Note>,
    searchQuery: String,
    sortOrder: SortOrder,
    settings: UserSettings,
    repository: NotesRepository,
    onSearch: (String) -> Unit,
    onToggleFavorite: (Long, Boolean) -> Unit,
    onDeleteNote: (Long) -> Unit,
    onToggleDarkTheme: () -> Unit,
    onSortOrderChange: (SortOrder) -> Unit,
    onRefreshNotes: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    var selectedDrawerItem by remember { mutableStateOf(DrawerItem.NOTES) }

    val showFab = currentRoute == BottomNavItem.Notes.route

    val screenTitle = when (currentRoute) {
        BottomNavItem.Notes.route -> "Catatan"
        BottomNavItem.Favorites.route -> "Favorit"
        BottomNavItem.Profile.route -> "Profil"
        Screen.AddNote.route -> "Tambah Catatan"
        Screen.Settings.route -> "Pengaturan"
        else -> if (currentRoute?.startsWith("note_detail") == true) "Detail Catatan"
                else if (currentRoute?.startsWith("edit_note") == true) "Edit Catatan"
                else "Notes App"
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            AppDrawer(
                selectedItem = selectedDrawerItem,
                onItemSelected = { item ->
                    selectedDrawerItem = item
                    scope.launch {
                        drawerState.close()
                    }
                    val route = when (item) {
                        DrawerItem.NOTES -> BottomNavItem.Notes.route
                        DrawerItem.FAVORITES -> BottomNavItem.Favorites.route
                        DrawerItem.PROFILE -> BottomNavItem.Profile.route
                        DrawerItem.SETTINGS -> Screen.Settings.route
                        DrawerItem.ABOUT -> BottomNavItem.Notes.route
                    }
                    if (item != DrawerItem.ABOUT) {
                        navController.navigate(route) {
                            popUpTo(BottomNavItem.Notes.route) { inclusive = false }
                            launchSingleTop = true
                        }
                    }
                }
            )
        },
        gesturesEnabled = drawerState.isOpen
    ) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(screenTitle) },
                    navigationIcon = {
                        IconButton(onClick = {
                            scope.launch {
                                drawerState.open()
                            }
                        }) {
                            Icon(
                                imageVector = Icons.Default.Settings,
                                contentDescription = "Menu"
                            )
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                )
            },
            floatingActionButton = {
                if (showFab) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.AddNote.route) },
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add Note"
                        )
                    }
                }
            },
            modifier = modifier
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = BottomNavItem.Notes.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                composable(BottomNavItem.Notes.route) {
                    selectedDrawerItem = DrawerItem.NOTES
                    NotesListScreen(
                        uiState = notesUiState,
                        searchQuery = searchQuery,
                        sortOrder = sortOrder,
                        onSearch = onSearch,
                        onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) },
                        onFavoriteToggle = onToggleFavorite,
                        onDelete = onDeleteNote,
                        onSettingsClick = { navController.navigate(Screen.Settings.route) },
                        onAddNote = { navController.navigate(Screen.AddNote.route) }
                    )
                }

                composable(BottomNavItem.Favorites.route) {
                    selectedDrawerItem = DrawerItem.FAVORITES
                    FavoritesScreen(
                        notes = favoriteNotes,
                        onNoteClick = { id -> navController.navigate(Screen.NoteDetail.createRoute(id)) },
                        onFavoriteToggle = onToggleFavorite,
                        onDelete = onDeleteNote
                    )
                }

                composable(BottomNavItem.Profile.route) {
                    selectedDrawerItem = DrawerItem.PROFILE
                    ProfileScreen()
                }

                composable(Screen.Settings.route) {
                    selectedDrawerItem = DrawerItem.SETTINGS
                    SettingsScreen(
                        settings = settings,
                        onBack = { navController.popBackStack() },
                        onToggleDarkTheme = onToggleDarkTheme,
                        onSortOrderChange = onSortOrderChange
                    )
                }

                composable(Screen.AddNote.route) {
                    NoteEditorNavHost(
                        repository = repository,
                        noteId = null,
                        onBack = { navController.popBackStack() },
                        onSaved = {
                            onRefreshNotes()
                            navController.popBackStack()
                        }
                    )
                }

                composable(
                    route = Screen.NoteDetail.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                    NoteDetailNavHost(
                        repository = repository,
                        noteId = noteId,
                        onBack = { navController.popBackStack() },
                        onEdit = { id -> navController.navigate(Screen.EditNote.createRoute(id)) },
                        onDeleted = {
                            onRefreshNotes()
                            navController.popBackStack(BottomNavItem.Notes.route, inclusive = false)
                        }
                    )
                }

                composable(
                    route = Screen.EditNote.route,
                    arguments = listOf(navArgument("noteId") { type = NavType.LongType })
                ) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getLong("noteId") ?: 0L
                    NoteEditorNavHost(
                        repository = repository,
                        noteId = noteId,
                        onBack = { navController.popBackStack() },
                        onSaved = {
                            onRefreshNotes()
                            navController.popBackStack()
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun NoteDetailNavHost(
    repository: NotesRepository,
    noteId: Long,
    onBack: () -> Unit,
    onEdit: (Long) -> Unit,
    onDeleted: () -> Unit
) {
    val viewModel = remember(noteId) {
        com.example.tugas7khairulrijalsyauqi.viewmodel.NoteDetailViewModel(repository, noteId)
    }
    val uiState by viewModel.uiState.collectAsState()

    NoteDetailScreen(
        note = uiState.note,
        isLoading = uiState.isLoading,
        onBack = onBack,
        onEdit = onEdit,
        onFavoriteToggle = { viewModel.toggleFavorite() },
        onDelete = { viewModel.deleteNote(onDeleted) }
    )
}

@Composable
private fun NoteEditorNavHost(
    repository: NotesRepository,
    noteId: Long?,
    onBack: () -> Unit,
    onSaved: () -> Unit
) {
    val viewModel = remember(noteId) {
        com.example.tugas7khairulrijalsyauqi.viewmodel.NoteEditorViewModel(repository, noteId)
    }
    val uiState by viewModel.uiState.collectAsState()

    if (noteId != null) {
        EditNoteScreen(
            uiState = uiState,
            onTitleChange = { viewModel.updateTitle(it) },
            onContentChange = { viewModel.updateContent(it) },
            onSave = { viewModel.save(onSaved) },
            onBack = onBack
        )
    } else {
        AddNoteScreen(
            uiState = uiState,
            onTitleChange = { viewModel.updateTitle(it) },
            onContentChange = { viewModel.updateContent(it) },
            onSave = { viewModel.save(onSaved) },
            onBack = onBack
        )
    }
}
package com.example.tugas7khairulrijalsyauqi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.tugas7khairulrijalsyauqi.database.createDatabase
import com.example.tugas7khairulrijalsyauqi.datastore.SettingsDataStore
import com.example.tugas7khairulrijalsyauqi.repository.NotesRepository

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val database = createDatabase(this)
        val repository = NotesRepository(database.notesDatabaseQueries)
        val settingsDataStore = SettingsDataStore(this.dataStore)

        setContent {
            App(repository, settingsDataStore)
        }
    }
}
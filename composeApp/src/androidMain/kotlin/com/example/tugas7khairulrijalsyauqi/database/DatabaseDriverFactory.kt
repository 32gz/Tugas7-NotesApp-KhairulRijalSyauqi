package com.example.tugas7khairulrijalsyauqi.database

import android.content.Context
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.example.tugas7khairulrijalsyauqi.NotesDatabase

fun createDatabase(context: Context): NotesDatabase {
    val driver = AndroidSqliteDriver(
        schema = NotesDatabase.Schema,
        context = context,
        name = "notes.db"
    )
    return NotesDatabase(driver)
}
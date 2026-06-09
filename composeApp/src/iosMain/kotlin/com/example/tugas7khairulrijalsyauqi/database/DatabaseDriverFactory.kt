package com.example.tugas7khairulrijalsyauqi.database

import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.example.tugas7khairulrijalsyauqi.NotesDatabase

fun createDatabase(): NotesDatabase {
    val driver = NativeSqliteDriver(
        schema = NotesDatabase.Schema,
        name = "notes.db"
    )
    return NotesDatabase(driver)
}
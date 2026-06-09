package com.example.tugas7khairulrijalsyauqi.database

import app.cash.sqldriver.ios.SqliteDriver

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        error("iOS database not implemented in this example")
    }
}

fun getDatabaseDriver(): SqlDriver {
    error("iOS database not implemented in this example")
}
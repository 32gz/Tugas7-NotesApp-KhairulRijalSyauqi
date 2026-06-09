package com.example.tugas7khairulrijalsyauqi

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object App {
    private var context: Context? = null

    fun init(context: Context) {
        this.context = context.applicationContext
    }

    fun getContext(): Context {
        return context ?: throw IllegalStateException("App not initialized")
    }
}
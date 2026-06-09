package com.example.tugas7khairulrijalsyauqi.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

data class UserProfile(
    val name: String = "Khairul Rijal Syauqi",
    val email: String = "khairul.123140143@student.itera.ac.id",
    val phone: String = "+62 813 8568 0425",
    val institution: String = "Institut Teknologi Sumatera"
)

class ProfileViewModel {
    var profile by mutableStateOf(UserProfile())
        private set

    var isEditing by mutableStateOf(false)
        private set

    fun startEditing() {
        isEditing = true
    }

    fun cancelEditing() {
        isEditing = false
    }

    fun updateName(name: String) {
        profile = profile.copy(name = name)
    }

    fun updateEmail(email: String) {
        profile = profile.copy(email = email)
    }

    fun updatePhone(phone: String) {
        profile = profile.copy(phone = phone)
    }

    fun updateInstitution(institution: String) {
        profile = profile.copy(institution = institution)
    }

    fun saveProfile() {
        isEditing = false
    }
}

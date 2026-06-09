package com.example.tugas7khairulrijalsyauqi

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
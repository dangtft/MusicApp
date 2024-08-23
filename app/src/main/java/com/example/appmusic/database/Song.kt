package com.example.appmusic.database

data class Song(
    val id: Int,
    val title: String,
    val artist:String,
    val duration: Int,
    val songFileResId: Int,
    val imageResId: Int
)

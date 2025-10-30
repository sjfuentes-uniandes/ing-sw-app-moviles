package com.example.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "artist_table")
data class Artist(
    @PrimaryKey val artistId: Int,
    val image: String,
    val name: String,
    val description: String,
    val creationDate: String
)

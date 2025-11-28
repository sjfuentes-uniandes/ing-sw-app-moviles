package com.example.vinilos.models

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "albums_table")
data class Album (
    @PrimaryKey val albumId:Int,
    val name:String,
    val cover:String,
    val releaseDate:String,
    val description:String,
    val genre:String,
    val recordLabel:String,
    @Ignore val tracks: List<Track> = emptyList()  // Lista de tracks (no se persiste en Room)
) {
    // Constructor secundario para Room (sin tracks)
    constructor(
        albumId: Int,
        name: String,
        cover: String,
        releaseDate: String,
        description: String,
        genre: String,
        recordLabel: String
    ) : this(albumId, name, cover, releaseDate, description, genre, recordLabel, emptyList())
}

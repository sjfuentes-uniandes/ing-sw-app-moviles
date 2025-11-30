package com.example.vinilos.models

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Modelo de datos para Track/Canción
 * Basado en la respuesta real de la API: GET /albums/{id}
 *
 * Ejemplo de Track en la API:
 * {
 *   "id": 100,
 *   "name": "Decisiones",
 *   "duration": "5:05"
 * }
 */
@Entity(tableName = "tracks_table")
data class Track(
    @PrimaryKey val id: Int,
    val name: String,
    val duration: String,
    val albumId: Int? = null  // Para relacionar con el álbum en la BD local
)


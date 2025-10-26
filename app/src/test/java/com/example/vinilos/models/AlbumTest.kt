package com.example.vinilos.models

import org.junit.Test
import org.junit.Assert.*

class AlbumTest {

    @Test
    fun album_creation_isCorrect() {
        val album = Album(
            albumId = 1,
            name = "Test Album",
            cover = "http://test.com/cover.jpg",
            releaseDate = "2023-01-01",
            description = "Test Description",
            genre = "Rock",
            recordLabel = "Test Label"
        )

        assertEquals(1, album.albumId)
        assertEquals("Test Album", album.name)
        assertEquals("http://test.com/cover.jpg", album.cover)
        assertEquals("2023-01-01", album.releaseDate)
        assertEquals("Test Description", album.description)
        assertEquals("Rock", album.genre)
        assertEquals("Test Label", album.recordLabel)
    }

    @Test
    fun album_equality_works() {
        val album1 = Album(1, "Test", "cover", "date", "desc", "genre", "label")
        val album2 = Album(1, "Test", "cover", "date", "desc", "genre", "label")
        
        assertEquals(album1, album2)
    }
}
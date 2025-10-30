package com.example.vinilos.models

import org.junit.Assert.*
import org.junit.Test

class ArtistTest {

    @Test
    fun artist_creation_isCorrect() {
        val artist = Artist(
            artistId = 1,
            name = "Test Artist",
            image = "http://test.com/image.jpg",
            description = "Test Description",
            creationDate = "1990-01-01",
        )

        assertEquals(1, artist.artistId)
        assertEquals("Test Artist", artist.name)
        assertEquals("http://test.com/image.jpg", artist.image)
        assertEquals("Test Description",artist.description)
        assertEquals("1990-01-01", artist.creationDate)
    }

    @Test
    fun artist_equality_works() {
        val artist1 = Artist(1, "Queen", "http://test.com/image.jpg", "Test Description", "1990-01-01")
        val artist2 = Artist(1, "Queen", "http://test.com/image.jpg", "Test Description", "1990-01-01")

        assertEquals(artist1,artist2)
    }
}
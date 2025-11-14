package com.example.vinilos.ui.adapters

import com.example.vinilos.models.Artist
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ArtistsAdapterTest {
    private lateinit var adapter: ArtistsAdapter
    private var clickedArtist: Artist? = null

    private val testArtists = listOf(
        Artist(1, "https://pm1.narvii.com/6724/a8b29909071e9d08517b40c748b6689649372852v2_hq.jpg","Queen","Quen es una banda","1970-01-01T00:00:00.000Z"),
        Artist(2, "https://pm2.narvii.com/6724/a8b29909071e9d08517b40c748b6689649372852v2_hq.jpg","AC-DC","Lorem ipsum","1989-02-15T00:00:00.000Z")
    )

    @Before
    fun setup() {
        clickedArtist = null
        adapter = ArtistsAdapter { artist -> clickedArtist = artist }
    }

    @Test
    fun `adapter starts with empty list`() {
        assertEquals(0, adapter.itemCount)
        assertTrue(adapter.artists.isEmpty())
    }

    @Test
    fun `getItemCount returns correct count`() {
        adapter.artists = testArtists
        assertEquals(testArtists.size, adapter.itemCount)
    }

    @Test
    fun `artists property getter works correctly`() {
        adapter.artists = testArtists
        assertEquals(testArtists, adapter.artists)
    }

    @Test
    fun `setting artists updates the list`() {
        assertEquals(0, adapter.itemCount)

        adapter.artists = testArtists

        assertEquals(testArtists.size, adapter.itemCount)
        assertEquals(testArtists, adapter.artists)
    }

    @Test
    fun `adapter can handle empty list after having items`() {
        adapter.artists = testArtists
        assertEquals(testArtists.size, adapter.itemCount)

        adapter.artists = emptyList()

        assertEquals(0, adapter.itemCount)
        assertTrue(adapter.artists.isEmpty())
    }
}
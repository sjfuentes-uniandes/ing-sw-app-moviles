package com.example.vinilos.ui.adapters

import com.example.vinilos.models.Artist
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*


class ArtistsAdapterTest {
    private lateinit var adapter: ArtistsAdapter
    private val testArtists = listOf(
        Artist(1, "https://pm1.narvii.com/6724/a8b29909071e9d08517b40c748b6689649372852v2_hq.jpg","Queen","Quen es una banda","1970-01-01T00:00:00.000Z"),
        Artist(2, "https://pm2.narvii.com/6724/a8b29909071e9d08517b40c748b6689649372852v2_hq.jpg","AC-DC","Lorem ipsum","1989-02-15T00:00:00.000Z")
    )

    @Before
    fun setup() {
        adapter = ArtistsAdapter()
    }

    @Test
    fun `adapter starts with empty list`(){
        assertEquals(0, adapter.itemCount)
        assertTrue(adapter.artists.isEmpty())
    }

    @Test
    fun `getItemCount returns correct count`(){
        val field = ArtistsAdapter::class.java.getDeclaredField("artists")
        field.isAccessible = true
        field.set(adapter, testArtists)

        assertEquals(testArtists.size,adapter.itemCount)
    }

    @Test
    fun `artists property getter works correctly`(){
        val field = ArtistsAdapter::class.java.getDeclaredField("artists")
        field.isAccessible = true
        field.set(adapter, testArtists)

        assertEquals(testArtists, adapter.artists)
    }
}
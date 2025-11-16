package com.example.vinilos.ui.adapters

import com.example.vinilos.models.Artist
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class ArtistsAdapterTest {
    private lateinit var adapter: ArtistsAdapter
    private var clickedArtist: Artist? = null

    @Before
    fun setup() {
        clickedArtist = null
        adapter = ArtistsAdapter { artist -> clickedArtist = artist }
    }

    @Test
    fun `adapter can be created with click listener`() {
        assertNotNull(adapter)
        assertNull(clickedArtist)
    }

}


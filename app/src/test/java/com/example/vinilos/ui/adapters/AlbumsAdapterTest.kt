package com.example.vinilos.ui.adapters

import com.example.vinilos.models.Album
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class AlbumsAdapterTest {

    private lateinit var adapter: AlbumsAdapter
    private val testAlbums = listOf(
        Album(1, "Album 1", "cover1", "2023-01-01", "desc1", "Rock", "Label1"),
        Album(2, "Album 2", "cover2", "2023-01-02", "desc2", "Pop", "Label2")
    )

    @Before
    fun setup() {
        adapter = AlbumsAdapter{}
    }

    @Test
    fun `adapter starts with empty list`() {
        assertEquals(0, adapter.itemCount)
        assertTrue(adapter.albums.isEmpty())
    }

    @Test
    fun `getItemCount returns correct count`() {
        adapter.albums = testAlbums
        assertEquals(testAlbums.size, adapter.itemCount)
    }

    @Test
    fun `albums property getter works correctly`() {
        adapter.albums = testAlbums
        assertEquals(testAlbums, adapter.albums)
    }
}
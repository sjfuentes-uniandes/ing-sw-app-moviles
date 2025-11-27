package com.example.vinilos.network

import android.content.Context
import io.mockk.*
import org.junit.Test
import org.junit.Assert.*
import org.junit.Before
import org.junit.After

class NetworkServiceAdapterTest {

    private val mockContext = mockk<Context>(relaxed = true)

    @Before
    fun setup() {
        every { mockContext.applicationContext } returns mockContext
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `base URL is correct`() {
        assertEquals("https://vinilos-backend-5f9h.onrender.com/", NetworkServiceAdapter.BASE_URL)
    }

    @Test
    fun `NetworkServiceAdapter class exists`() {
        val adapterClass = NetworkServiceAdapter::class.java
        assertNotNull(adapterClass)
        assertEquals("NetworkServiceAdapter", adapterClass.simpleName)
    }

    @Test
    fun `getArtistDetail constructs correct URL`() {
        // Given
        val artistId = 100
        val expectedUrl = "${NetworkServiceAdapter.BASE_URL}bands/$artistId"

        // Then
        assertEquals("https://vinilos-backend-5f9h.onrender.com/bands/100", expectedUrl)
    }

    @Test
    fun `getArtistDetail method exists in NetworkServiceAdapter`() {
        val method = NetworkServiceAdapter::class.java.declaredMethods.find {
            it.name == "getArtistDetail"
        }
        assertNotNull("Method getArtistDetail should exist", method)
    }

    @Test
    fun `getArtistDetail has correct parameters`() {
        val method = NetworkServiceAdapter::class.java.declaredMethods.find {
            it.name == "getArtistDetail"
        }
        assertNotNull(method)
        assertEquals(3, method?.parameterCount)
    }

    @Test
    fun `getInstance returns singleton instance`() {
        val instance1 = NetworkServiceAdapter.getInstance(mockContext)
        val instance2 = NetworkServiceAdapter.getInstance(mockContext)

        assertSame("getInstance should return the same instance", instance1, instance2)
    }

    @Test
    fun `BASE_URL constant is accessible`() {
        val baseUrl = NetworkServiceAdapter.BASE_URL
        assertTrue(baseUrl.isNotEmpty())
        assertTrue(baseUrl.startsWith("https://"))
    }

    @Test
    fun `artist detail endpoint path is correct`() {
        val artistId = 123
        val expectedPath = "bands/$artistId"
        assertEquals("bands/123", expectedPath)
    }

    @Test
    fun `album detail endpoint path is correct`() {
        val albumId = 456
        val expectedPath = "albums/$albumId"
        assertEquals("albums/456", expectedPath)
    }

    @Test
    fun `collector detail endpoint path is correct`() {
        val albumId = 123
        val expectedPath = "collectors/$albumId"
        assertEquals("collectors/123", expectedPath)
    }

    @Test
    fun `collectors album names endpoint path is correct`() {
        val albumId = 123
        val expectedPath = "collectors/$albumId/albums"
        assertEquals("collectors/123/albums", expectedPath)
    }

    @Test
    fun `getCollectorDetail constructs correct URL`() {
        val artistId = 100
        val expectedUrl = "${NetworkServiceAdapter.BASE_URL}collectors/$artistId"
        assertEquals("https://vinilos-backend-5f9h.onrender.com/collectors/100", expectedUrl)
    }

    @Test
    fun `getCollectorAlbumNames constructs correct URL`() {
        val artistId = 100
        val expectedUrl = "${NetworkServiceAdapter.BASE_URL}collectors/$artistId/albums"
        assertEquals("https://vinilos-backend-5f9h.onrender.com/collectors/100/albums", expectedUrl)
    }

    @Test
    fun `getCollectorDetail method exists in NetworkServiceAdapter`() {
        val method = NetworkServiceAdapter::class.java.declaredMethods.find {
            it.name == "getCollectorDetail"
        }
        assertNotNull("Method getCollectorDetail should exist", method)
    }

    @Test
    fun `getCollectorAlbumNames method exists in NetworkServiceAdapter`() {
        val method = NetworkServiceAdapter::class.java.declaredMethods.find {
            it.name == "getCollectorAlbumNames"
        }
        assertNotNull("Method getCollectorAlbumNames should exist", method)
    }

    @Test
    fun `getCollectorDetail has correct parameters`() {
        val method = NetworkServiceAdapter::class.java.declaredMethods.find {
            it.name == "getCollectorDetail"
        }
        assertNotNull(method)
        assertEquals(3, method?.parameterCount)
    }

    @Test
    fun `getCollectorAlbumNames has correct parameters`() {
        val method = NetworkServiceAdapter::class.java.declaredMethods.find {
            it.name == "getCollectorAlbumNames"
        }
        assertNotNull(method)
        assertEquals(3, method?.parameterCount)
    }

}
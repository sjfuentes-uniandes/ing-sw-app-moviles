package com.example.vinilos.network

import org.junit.Test
import org.junit.Assert.*

class NetworkServiceAdapterTest {

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
}
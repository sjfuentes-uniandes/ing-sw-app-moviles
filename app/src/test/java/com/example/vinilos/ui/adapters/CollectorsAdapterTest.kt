package com.example.vinilos.ui.adapters

import com.example.vinilos.models.Collector
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.Assert.*

class CollectorsAdapterTest {

    private lateinit var adapter: CollectorsAdapter
    private val testCollectors = listOf(
        Collector(1, "Collector 1", "123456789", "collector1@example.com"),
        Collector(2, "Collector 2", "987654321", "collector2@example.com")
    )

    @Before
    fun setup() {
        adapter = CollectorsAdapter()
    }

    @Test
    fun `adapter starts with empty list`() {
        assertEquals(0, adapter.itemCount)
        assertTrue(adapter.collectors.isEmpty())
    }

    @Test
    fun `getItemCount returns correct count`() {
        adapter.collectors = testCollectors
        assertEquals(testCollectors.size, adapter.itemCount)
    }

    @Test
    fun `collectors property getter works correctly`() {
        adapter.collectors = testCollectors
        assertEquals(testCollectors, adapter.collectors)
    }
}
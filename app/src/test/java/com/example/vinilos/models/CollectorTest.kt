package com.example.vinilos.models

import org.junit.Test
import org.junit.Assert.*

class CollectorTest {

    @Test
    fun collector_creation_isCorrect() {
        val collector = Collector(
            collectorId = 1,
            name = "Test Collector",
            telephone = "123456789",
            email = "test@example.com"
        )

        assertEquals(1, collector.collectorId)
        assertEquals("Test Collector", collector.name)
        assertEquals("123456789", collector.telephone)
        assertEquals("test@example.com", collector.email)
    }

    @Test
    fun collector_equality_works() {
        val collector1 = Collector(1, "Test", "123", "test@example.com")
        val collector2 = Collector(1, "Test", "123", "test@example.com")
        
        assertEquals(collector1, collector2)
    }
}
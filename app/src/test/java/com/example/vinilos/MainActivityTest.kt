package com.example.vinilos

import org.junit.Test
import org.junit.Assert.*

class MainActivityTest {

    @Test
    fun `activity class exists`() {
        val activityClass = MainActivity::class.java
        assertNotNull(activityClass)
        assertEquals("MainActivity", activityClass.simpleName)
    }
}
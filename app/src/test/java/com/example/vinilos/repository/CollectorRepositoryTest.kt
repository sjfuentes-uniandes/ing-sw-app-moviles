package com.example.vinilos.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.vinilos.database.CollectorsDao
import com.example.vinilos.models.Collector
import com.example.vinilos.network.NetworkServiceAdapter
import com.android.volley.VolleyError
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class CollectorRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: CollectorRepository
    private val mockDao = mockk<CollectorsDao>()
    private val mockNetworkService = mockk<NetworkServiceAdapter>()
    private val collectorsLiveData = MutableLiveData<List<Collector>>()

    @Before
    fun setup() {
        every { mockDao.getCollectors() } returns collectorsLiveData
        every { mockDao.insertAll(any()) } just Runs
        every { mockDao.deleteAll() } just Runs
        
        repository = CollectorRepository(mockDao, mockNetworkService)
    }

    @Test
    fun `getCollectors returns LiveData from DAO`() {
        val result = repository.getCollectors()
        
        assertEquals(collectorsLiveData, result)
        verify { mockDao.getCollectors() }
    }

    @Test
    fun `refreshCollectors success clears cache and inserts new data`() = runTest {
        val testCollectors = listOf(
            Collector(1, "Collector 1", "123456789", "collector1@example.com"),
            Collector(2, "Collector 2", "987654321", "collector2@example.com")
        )
        
        every { mockNetworkService.getCollectors(any(), any()) } answers {
            val onComplete = firstArg<(List<Collector>) -> Unit>()
            onComplete(testCollectors)
        }

        val result = repository.refreshCollectors()

        assertTrue(result.isSuccess)
        verifyOrder {
            mockDao.deleteAll()
            mockDao.insertAll(testCollectors)
        }
    }

    @Test
    fun `refreshCollectors failure returns error result`() = runTest {
        val testError = VolleyError("Network error")
        
        every { mockNetworkService.getCollectors(any(), any()) } answers {
            val onError = secondArg<(VolleyError) -> Unit>()
            onError(testError)
        }

        val result = repository.refreshCollectors()

        assertTrue(result.isFailure)
        verify(exactly = 0) { mockDao.deleteAll() }
        verify(exactly = 0) { mockDao.insertAll(any()) }
    }
}
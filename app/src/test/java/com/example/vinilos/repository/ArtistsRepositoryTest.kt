package com.example.vinilos.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.android.volley.VolleyError
import com.example.vinilos.database.ArtistsDao
import com.example.vinilos.models.Artist
import com.example.vinilos.network.NetworkServiceAdapter
import io.mockk.Runs
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import io.mockk.verifyOrder
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class ArtistsRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: ArtistsRepository
    private val mockDao = mockk<ArtistsDao>()
    private val mockNetworkService = mockk<NetworkServiceAdapter>()
    private val artistLiveData = MutableLiveData<List<Artist>>()

    @Before
    fun setup() {
        every { mockDao.getArtists() } returns artistLiveData
        every { mockDao.insertAll(any()) } just Runs
        every { mockDao.deleteAll() } just Runs

        repository = ArtistsRepository(mockDao, mockNetworkService)
    }

    @Test
    fun `getArtists returns LiveData from DAO`()
    {
        val result = repository.getArtists()

        assertEquals(artistLiveData,result)
        verify { mockDao.getArtists() }
    }

    @Test
    fun `refreshArtists success clears cache and inserts new data`() = runTest {
        val testArtists = listOf(
            Artist(1, "https://pm1.narvii.com/6724/a8b29909071e9d08517b40c748b6689649372852v2_hq.jpg","Queen","Quen es una banda","1970-01-01T00:00:00.000Z"),
            Artist(2, "https://pm2.narvii.com/6724/a8b29909071e9d08517b40c748b6689649372852v2_hq.jpg","AC-DC","Lorem ipsum","1989-02-15T00:00:00.000Z")
        )

        every { mockNetworkService.getArtists(any(),any()) } answers {
            val onComplete = firstArg<(List<Artist>) -> Unit>()
            onComplete(testArtists)
        }

        val result = repository.refreshArtists()

        assertTrue(result.isSuccess)
        verifyOrder {
            mockDao.deleteAll()
            mockDao.insertAll(testArtists)
        }
    }

    @Test
    fun `refreshArtists failure returns error result`() = runTest {
        val testError = VolleyError("Network error")
        every { mockNetworkService.getArtists(any(),any()) } answers {
            val onError = secondArg<(VolleyError) -> Unit>()
            onError(testError)
        }

        val result = repository.refreshArtists()

        assertTrue(result.isFailure)
        verify(exactly = 0){ mockDao.deleteAll() }
        verify(exactly = 0){ mockDao.insertAll(any()) }
    }


}
package com.example.vinilos.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.vinilos.database.AlbumsDao
import com.example.vinilos.models.Album
import com.example.vinilos.network.NetworkServiceAdapter
import com.android.volley.VolleyError
import io.mockk.*
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class AlbumRepositoryTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: AlbumRepository
    private val mockDao = mockk<AlbumsDao>()
    private val mockNetworkService = mockk<NetworkServiceAdapter>()
    private val albumsLiveData = MutableLiveData<List<Album>>()

    @Before
    fun setup() {
        every { mockDao.getAlbums() } returns albumsLiveData
        every { mockDao.insertAll(any()) } just Runs
        every { mockDao.deleteAll() } just Runs
        
        repository = AlbumRepository(mockDao, mockNetworkService)
    }

    @Test
    fun `getAlbums returns LiveData from DAO`() {
        val result = repository.getAlbums()
        
        assertEquals(albumsLiveData, result)
        verify { mockDao.getAlbums() }
    }

    @Test
    fun `refreshAlbums success clears cache and inserts new data`() = runTest {
        val testAlbums = listOf(
            Album(1, "Album 1", "cover1", "2023-01-01", "desc1", "Rock", "Label1"),
            Album(2, "Album 2", "cover2", "2023-01-02", "desc2", "Pop", "Label2")
        )
        
        every { mockNetworkService.getAlbums(any(), any()) } answers {
            val onComplete = firstArg<(List<Album>) -> Unit>()
            onComplete(testAlbums)
        }

        val result = repository.refreshAlbums()

        assertTrue(result.isSuccess)
        verifyOrder {
            mockDao.deleteAll()
            mockDao.insertAll(testAlbums)
        }
    }

    @Test
    fun `refreshAlbums failure returns error result`() = runTest {
        val testError = VolleyError("Network error")
        
        every { mockNetworkService.getAlbums(any(), any()) } answers {
            val onError = secondArg<(VolleyError) -> Unit>()
            onError(testError)
        }

        val result = repository.refreshAlbums()

        assertTrue(result.isFailure)
        verify(exactly = 0) { mockDao.deleteAll() }
        verify(exactly = 0) { mockDao.insertAll(any()) }
    }
}
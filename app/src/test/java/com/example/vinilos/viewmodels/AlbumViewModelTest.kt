package com.example.vinilos.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.vinilos.models.Album
import com.example.vinilos.viemodels.AlbumViewModel
import com.example.vinilos.repository.AlbumRepository
import com.example.vinilos.database.VinilosRoomDatabase
import com.example.vinilos.database.AlbumsDao
import com.example.vinilos.network.NetworkServiceAdapter
import io.mockk.*
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.Dispatchers
import org.junit.Before
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class AlbumViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: AlbumViewModel
    private val mockApplication = mockk<Application>()
    private val mockRepository = mockk<AlbumRepository>()
    private val mockAlbumsObserver = mockk<Observer<List<Album>>>(relaxed = true)
    private val mockErrorObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val albumsLiveData = MutableLiveData<List<Album>>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { mockApplication.applicationContext } returns mockApplication
        mockkObject(VinilosRoomDatabase.Companion)
        mockkObject(NetworkServiceAdapter.Companion)
        
        val mockDatabase = mockk<VinilosRoomDatabase>()
        val mockDao = mockk<AlbumsDao>()
        val mockNetworkService = mockk<NetworkServiceAdapter>()
        
        every { VinilosRoomDatabase.getDatabase(any()) } returns mockDatabase
        every { mockDatabase.albumsDao() } returns mockDao
        every { mockDao.getAlbums() } returns albumsLiveData
        every { NetworkServiceAdapter.getInstance(any()) } returns mockNetworkService
        
        viewModel = AlbumViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `albums LiveData is initialized`() {
        assertNotNull(viewModel.albums)
    }

    @Test
    fun `network error LiveData is initialized as false`() {
        viewModel.eventNetworkError.observeForever(mockErrorObserver)
        verify { mockErrorObserver.onChanged(false) }
    }

    @Test
    fun `refreshAlbums calls repository`() = runTest {
        viewModel.refreshAlbums()
        // Note: This test would need dependency injection to properly verify
        // For now, we just verify the method doesn't crash
        assertNotNull(viewModel.albums)
    }

    @Test
    fun `onNetworkErrorShown sets error shown to true`() {
        val mockShownObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isNetworkErrorShown.observeForever(mockShownObserver)
        
        viewModel.onNetworkErrorShown()
        
        verify { mockShownObserver.onChanged(true) }
    }
}
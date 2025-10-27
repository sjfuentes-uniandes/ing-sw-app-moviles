package com.example.vinilos.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.vinilos.models.Album
import com.example.vinilos.network.NetworkServiceAdapter
import com.example.vinilos.viemodels.AlbumViewModel
import com.example.vinilos.database.VinilosRoomDatabase
import com.example.vinilos.database.AlbumsDao
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class AlbumViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AlbumViewModel
    private val mockApplication = mockk<Application>()
    private val mockNetworkAdapter = mockk<NetworkServiceAdapter>()
    private val mockDatabase = mockk<VinilosRoomDatabase>()
    private val mockAlbumsDao = mockk<AlbumsDao>()
    private val mockAlbumsObserver = mockk<Observer<List<Album>>>(relaxed = true)
    private val mockErrorObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val albumsLiveData = MutableLiveData<List<Album>>()

    @Before
    fun setup() {
        mockkObject(NetworkServiceAdapter.Companion)
        mockkObject(VinilosRoomDatabase.Companion)
        
        every { NetworkServiceAdapter.getInstance(any()) } returns mockNetworkAdapter
        every { mockNetworkAdapter.getAlbums(any(), any()) } just Runs
        every { VinilosRoomDatabase.getDatabase(any()) } returns mockDatabase
        every { mockDatabase.albumsDao() } returns mockAlbumsDao
        every { mockAlbumsDao.getAlbums() } returns albumsLiveData
        every { mockAlbumsDao.insertAll(any()) } just Runs
        
        viewModel = AlbumViewModel(mockApplication)
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
    fun `refreshAlbums calls network service`() {
        viewModel.refreshAlbums()
        verify { mockNetworkAdapter.getAlbums(any(), any()) }
    }

    @Test
    fun `onNetworkErrorShown sets error shown to true`() {
        val mockShownObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isNetworkErrorShown.observeForever(mockShownObserver)
        
        viewModel.onNetworkErrorShown()
        
        verify { mockShownObserver.onChanged(true) }
    }
}
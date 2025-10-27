package com.example.vinilos.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.example.vinilos.models.Album
import com.example.vinilos.network.NetworkServiceAdapter
import com.example.vinilos.viemodels.AlbumViewModel
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
    private val mockAlbumsObserver = mockk<Observer<List<Album>>>(relaxed = true)
    private val mockErrorObserver = mockk<Observer<Boolean>>(relaxed = true)

    @Before
    fun setup() {
        mockkObject(NetworkServiceAdapter.Companion)
        every { NetworkServiceAdapter.getInstance(any()) } returns mockNetworkAdapter
        every { mockNetworkAdapter.getAlbums(any(), any()) } just Runs
        
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
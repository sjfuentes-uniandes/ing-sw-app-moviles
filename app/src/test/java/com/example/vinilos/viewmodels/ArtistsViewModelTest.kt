package com.example.vinilos.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import com.example.vinilos.database.ArtistsDao
import com.example.vinilos.database.VinilosRoomDatabase
import com.example.vinilos.models.Artist
import com.example.vinilos.network.NetworkServiceAdapter
import com.example.vinilos.repository.ArtistsRepository
import com.example.vinilos.viemodels.ArtistsViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.test.resetMain
import org.junit.Before
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class ArtistsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ArtistsViewModel
    private val mockApplication = mockk<Application>()
    private val mockRepository = mockk<ArtistsRepository>()
    private val mockArtistsObserver = mockk<Observer<List<Artist>>>(relaxed = true)
    private val mockErrorObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val artistLiveData = MutableLiveData<List<Artist>>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { mockApplication.applicationContext } returns mockApplication
        mockkObject(VinilosRoomDatabase.Companion)
        mockkObject(NetworkServiceAdapter.Companion)

        val mockDatabase = mockk<VinilosRoomDatabase>()
        val mockDao = mockk<ArtistsDao>()
        val mockNetworkService = mockk<NetworkServiceAdapter>()

        every { VinilosRoomDatabase.getDatabase(any()) } returns mockDatabase
        every { mockDatabase.artistsDao() } returns mockDao
        every { mockDao.getArtists() } returns artistLiveData
        every { NetworkServiceAdapter.getInstance(any()) } returns mockNetworkService

        viewModel = ArtistsViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `artists LiveData is initialized`() {
        assertNotNull(viewModel.artist)
    }

    @Test
    fun `network error LiveData is initialized as false`() {
        viewModel.eventNetworkError.observeForever(mockErrorObserver)
        verify { mockErrorObserver.onChanged(false) }
    }

    @Test
    fun `refreshArtists calls repository`() = runTest {
        viewModel.refreshArtists()
        assertNotNull(viewModel.artist)
    }

    @Test
    fun `onNetworkErrorShown sets error shown to true`() {
        val mockShownObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isNetworkErrorShown.observeForever(mockShownObserver)
        viewModel.onNetworkErrorShown()
        verify { mockShownObserver.onChanged(true) }
    }











}
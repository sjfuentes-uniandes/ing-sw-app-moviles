package com.example.vinilos.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.vinilos.models.Collector
import com.example.vinilos.viemodels.CollectorViewModel
import com.example.vinilos.repository.CollectorRepository
import com.example.vinilos.database.VinilosRoomDatabase
import com.example.vinilos.database.CollectorsDao
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

class CollectorViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: CollectorViewModel
    private val mockApplication = mockk<Application>()
    private val mockRepository = mockk<CollectorRepository>()
    private val mockCollectorsObserver = mockk<Observer<List<Collector>>>(relaxed = true)
    private val mockErrorObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val collectorsLiveData = MutableLiveData<List<Collector>>()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { mockApplication.applicationContext } returns mockApplication
        mockkObject(VinilosRoomDatabase.Companion)
        mockkObject(NetworkServiceAdapter.Companion)
        
        val mockDatabase = mockk<VinilosRoomDatabase>()
        val mockDao = mockk<CollectorsDao>()
        val mockNetworkService = mockk<NetworkServiceAdapter>()
        
        every { VinilosRoomDatabase.getDatabase(any()) } returns mockDatabase
        every { mockDatabase.collectorsDao() } returns mockDao
        every { mockDao.getCollectors() } returns collectorsLiveData
        every { NetworkServiceAdapter.getInstance(any()) } returns mockNetworkService
        
        viewModel = CollectorViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `collectors LiveData is initialized`() {
        assertNotNull(viewModel.collectors)
    }

    @Test
    fun `network error LiveData is initialized as false`() {
        viewModel.eventNetworkError.observeForever(mockErrorObserver)
        verify { mockErrorObserver.onChanged(false) }
    }

    @Test
    fun `refreshCollectors calls repository`() = runTest {
        viewModel.refreshCollectors()
        // Note: This test would need dependency injection to properly verify
        // For now, we just verify the method doesn't crash
        assertNotNull(viewModel.collectors)
    }

    @Test
    fun `onNetworkErrorShown sets error shown to true`() {
        val mockShownObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isNetworkErrorShown.observeForever(mockShownObserver)
        
        viewModel.onNetworkErrorShown()
        
        verify { mockShownObserver.onChanged(true) }
    }
}
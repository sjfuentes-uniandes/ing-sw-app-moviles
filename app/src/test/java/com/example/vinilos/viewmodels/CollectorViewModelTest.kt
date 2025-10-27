package com.example.vinilos.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.vinilos.models.Collector
import com.example.vinilos.network.NetworkServiceAdapter
import com.example.vinilos.viemodels.CollectorViewModel
import com.example.vinilos.database.VinilosRoomDatabase
import com.example.vinilos.database.CollectorsDao
import io.mockk.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class CollectorViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CollectorViewModel
    private val mockApplication = mockk<Application>()
    private val mockNetworkAdapter = mockk<NetworkServiceAdapter>()
    private val mockDatabase = mockk<VinilosRoomDatabase>()
    private val mockCollectorsDao = mockk<CollectorsDao>()
    private val mockCollectorsObserver = mockk<Observer<List<Collector>>>(relaxed = true)
    private val mockErrorObserver = mockk<Observer<Boolean>>(relaxed = true)
    private val collectorsLiveData = MutableLiveData<List<Collector>>()

    @Before
    fun setup() {
        mockkObject(NetworkServiceAdapter.Companion)
        mockkObject(VinilosRoomDatabase.Companion)
        
        every { NetworkServiceAdapter.getInstance(any()) } returns mockNetworkAdapter
        every { mockNetworkAdapter.getCollectors(any(), any()) } just Runs
        every { VinilosRoomDatabase.getDatabase(any()) } returns mockDatabase
        every { mockDatabase.collectorsDao() } returns mockCollectorsDao
        every { mockCollectorsDao.getCollectors() } returns collectorsLiveData
        every { mockCollectorsDao.insertAll(any()) } just Runs
        
        viewModel = CollectorViewModel(mockApplication)
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
    fun `refreshCollectors calls network service`() {
        viewModel.refreshCollectors()
        verify { mockNetworkAdapter.getCollectors(any(), any()) }
    }

    @Test
    fun `onNetworkErrorShown sets error shown to true`() {
        val mockShownObserver = mockk<Observer<Boolean>>(relaxed = true)
        viewModel.isNetworkErrorShown.observeForever(mockShownObserver)
        
        viewModel.onNetworkErrorShown()
        
        verify { mockShownObserver.onChanged(true) }
    }
}
package com.example.vinilos.ui.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class DashboardViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DashboardViewModel
    private val mockObserver = mockk<Observer<String>>(relaxed = true)

    @Before
    fun setup() {
        viewModel = DashboardViewModel()
    }

    @Test
    fun `text LiveData has initial value`() {
        viewModel.text.observeForever(mockObserver)
        
        verify { mockObserver.onChanged("This is dashboard Fragment") }
    }

    @Test
    fun `text LiveData is not null`() {
        assertNotNull(viewModel.text)
    }
}
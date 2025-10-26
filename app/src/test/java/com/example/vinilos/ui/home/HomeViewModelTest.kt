package com.example.vinilos.ui.home

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class HomeViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: HomeViewModel
    private val mockObserver = mockk<Observer<String>>(relaxed = true)

    @Before
    fun setup() {
        viewModel = HomeViewModel()
    }

    @Test
    fun `text LiveData has initial value`() {
        viewModel.text.observeForever(mockObserver)
        
        verify { mockObserver.onChanged("This is home Fragment") }
    }

    @Test
    fun `text LiveData is not null`() {
        assertNotNull(viewModel.text)
    }
}
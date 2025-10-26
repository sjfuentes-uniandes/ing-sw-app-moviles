package com.example.vinilos.ui.notifications

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

class NotificationsViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: NotificationsViewModel
    private val mockObserver = mockk<Observer<String>>(relaxed = true)

    @Before
    fun setup() {
        viewModel = NotificationsViewModel()
    }

    @Test
    fun `text LiveData has initial value`() {
        viewModel.text.observeForever(mockObserver)
        
        verify { mockObserver.onChanged("This is notifications Fragment") }
    }

    @Test
    fun `text LiveData is not null`() {
        assertNotNull(viewModel.text)
    }
}
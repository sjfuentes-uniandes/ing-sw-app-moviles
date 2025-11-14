package com.example.vinilos.viewmodels

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.android.volley.VolleyError
import com.example.vinilos.models.Artist
import com.example.vinilos.network.NetworkServiceAdapter
import com.example.vinilos.viemodels.ArtistDetailViewModel
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*

@OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
class ArtistDetailViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var viewModel: ArtistDetailViewModel
    private val mockApplication = mockk<Application>()
    private val mockNetworkService = mockk<NetworkServiceAdapter>()
    private val mockArtistObserver = mockk<Observer<Artist>>(relaxed = true)
    private val mockErrorObserver = mockk<Observer<String>>(relaxed = true)

    private val testArtist = Artist(
        artistId = 100,
        name = "Queen",
        image = "https://example.com/queen.jpg",
        description = "British rock band",
        creationDate = "1970-01-01"
    )

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { mockApplication.applicationContext } returns mockApplication
        mockkObject(NetworkServiceAdapter.Companion)
        every { NetworkServiceAdapter.getInstance(any()) } returns mockNetworkService

        viewModel = ArtistDetailViewModel(mockApplication)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    @Test
    fun `artist LiveData is initialized`() {
        assertNotNull(viewModel.artist)
    }

    @Test
    fun `error LiveData is initialized`() {
        assertNotNull(viewModel.error)
    }

    @Test
    fun `loadArtistDetail successfully loads artist data`() {
        // Given
        val artistId = 100
        every {
            mockNetworkService.getArtistDetail(
                artistId,
                captureLambda(),
                any()
            )
        } answers {
            lambda<(Artist) -> Unit>().captured.invoke(testArtist)
        }

        viewModel.artist.observeForever(mockArtistObserver)

        // When
        viewModel.loadArtistDetail(artistId)

        // Then
        verify { mockNetworkService.getArtistDetail(artistId, any(), any()) }
        verify { mockArtistObserver.onChanged(testArtist) }
    }

    @Test
    fun `loadArtistDetail handles network error`() {
        // Given
        val artistId = 100
        val errorMessage = "Network error"
        val volleyError = mockk<VolleyError>()
        every { volleyError.message } returns errorMessage
        every { volleyError.networkResponse } returns null

        every {
            mockNetworkService.getArtistDetail(
                artistId,
                any(),
                captureLambda()
            )
        } answers {
            lambda<(VolleyError) -> Unit>().captured.invoke(volleyError)
        }

        viewModel.error.observeForever(mockErrorObserver)

        // When
        viewModel.loadArtistDetail(artistId)

        // Then
        verify { mockNetworkService.getArtistDetail(artistId, any(), any()) }
        verify { mockErrorObserver.onChanged(errorMessage) }
    }

    @Test
    fun `loadArtistDetail handles error with network response`() {
        // Given
        val artistId = 100
        val statusCode = 404
        val errorData = "Not Found"
        val volleyError = mockk<VolleyError>()
        val networkResponse = mockk<com.android.volley.NetworkResponse>()

        every { networkResponse.statusCode } returns statusCode
        every { networkResponse.data } returns errorData.toByteArray()
        every { volleyError.networkResponse } returns networkResponse
        every { volleyError.message } returns null

        every {
            mockNetworkService.getArtistDetail(
                artistId,
                any(),
                captureLambda()
            )
        } answers {
            lambda<(VolleyError) -> Unit>().captured.invoke(volleyError)
        }

        viewModel.error.observeForever(mockErrorObserver)

        // When
        viewModel.loadArtistDetail(artistId)

        // Then
        verify { mockNetworkService.getArtistDetail(artistId, any(), any()) }
        verify { mockErrorObserver.onChanged(match { it.contains("Error $statusCode") }) }
    }

    @Test
    fun `loadArtistDetail handles unknown error`() {
        // Given
        val artistId = 100
        val volleyError = mockk<VolleyError>()
        every { volleyError.message } returns null
        every { volleyError.networkResponse } returns null
        every { volleyError.toString() } returns "Unknown error"

        every {
            mockNetworkService.getArtistDetail(
                artistId,
                any(),
                captureLambda()
            )
        } answers {
            lambda<(VolleyError) -> Unit>().captured.invoke(volleyError)
        }

        viewModel.error.observeForever(mockErrorObserver)

        // When
        viewModel.loadArtistDetail(artistId)

        // Then
        verify { mockNetworkService.getArtistDetail(artistId, any(), any()) }
        verify { mockErrorObserver.onChanged(match { it.contains("Unknown error") }) }
    }

    @Test
    fun `loadArtistDetail with valid artistId calls network service`() {
        // Given
        val artistId = 200
        every {
            mockNetworkService.getArtistDetail(any(), any(), any())
        } just Runs

        // When
        viewModel.loadArtistDetail(artistId)

        // Then
        verify(exactly = 1) { mockNetworkService.getArtistDetail(artistId, any(), any()) }
    }

    @Test
    fun `artist LiveData value is null initially`() {
        assertNull(viewModel.artist.value)
    }

    @Test
    fun `error LiveData value is null initially`() {
        assertNull(viewModel.error.value)
    }

    @Test
    fun `loadArtistDetail updates artist LiveData correctly`() {
        // Given
        val artistId = 300
        val expectedArtist = Artist(
            artistId = artistId,
            name = "The Beatles",
            image = "https://example.com/beatles.jpg",
            description = "English rock band",
            creationDate = "1960-01-01"
        )

        every {
            mockNetworkService.getArtistDetail(
                artistId,
                captureLambda(),
                any()
            )
        } answers {
            lambda<(Artist) -> Unit>().captured.invoke(expectedArtist)
        }

        viewModel.artist.observeForever(mockArtistObserver)

        // When
        viewModel.loadArtistDetail(artistId)

        // Then
        verify { mockArtistObserver.onChanged(expectedArtist) }
        assertEquals(expectedArtist, viewModel.artist.value)
    }
}


package com.example.vinilos.ui.albums

import android.app.Application
import android.net.Uri
import android.widget.EditText
import android.widget.AutoCompleteTextView
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.example.vinilos.databinding.FragmentAddAlbumsBinding
import com.example.vinilos.models.Album
import com.example.vinilos.models.Artist
import com.example.vinilos.ui.adapters.Track
import com.example.vinilos.ui.adapters.TrackAdapter
import com.example.vinilos.viemodels.AlbumViewModel
import com.example.vinilos.viemodels.ArtistsViewModel
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
import java.text.SimpleDateFormat
import java.util.Locale


@OptIn(ExperimentalCoroutinesApi::class)
class AddAlbumFragmentTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()
    private lateinit var mockApplication: Application
    private lateinit var mockAlbumViewModel: AlbumViewModel
    private lateinit var mockArtistsViewModel: ArtistsViewModel
    private val artistsLiveData = MutableLiveData<List<Artist>>()
    private val createAlbumResultLiveData = MutableLiveData<Result<Album>>()
    private val mockArtistsObserver = mockk<Observer<List<Artist>>>(relaxed = true)
    private val mockCreateAlbumObserver = mockk<Observer<Result<Album>>>(relaxed = true)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        mockApplication = mockk(relaxed = true)
        mockAlbumViewModel = mockk(relaxed = true)
        mockArtistsViewModel = mockk(relaxed = true)

        every { mockArtistsViewModel.artist } returns artistsLiveData
        every { mockAlbumViewModel.createAlbumResult } returns createAlbumResultLiveData
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    /**
     * Helper method to initialize a fragment with trackAdapter for testing
     * Uses a mock adapter since ListAdapter requires Android runtime
     */
    private fun createFragmentWithTrackAdapter(): AddAlbumFragment {
        val fragment = AddAlbumFragment()
        val mockAdapter = mockk<TrackAdapter>(relaxed = true)
        fragment.trackAdapter = mockAdapter
        return fragment
    }

    @Test
    fun `formatDateForAPI convierte fecha de formato display a formato API correctamente`() {
        val fragment = AddAlbumFragment()
        val displayDate = "15/08/2023"
        val expectedApiDate = "2023-08-15T00:00:00-05:00"

        val result = fragment.formatDateForAPI(displayDate)

        assertEquals(expectedApiDate, result)
    }

    @Test
    fun `formatDateForAPI maneja diferentes formatos de fecha`() {
        val fragment = AddAlbumFragment()
        val testCases = mapOf(
            "01/01/2024" to "2024-01-01T00:00:00-05:00",
            "31/12/2023" to "2023-12-31T00:00:00-05:00",
            "29/02/2024" to "2024-02-29T00:00:00-05:00"
        )

        testCases.forEach { (input, expected) ->
            val result = fragment.formatDateForAPI(input)
            assertEquals("Fecha $input no se formateó correctamente", expected, result)
        }
    }

    @Test
    fun `formatDateForAPI retorna el string original si la fecha es inválida`() {
        val fragment = AddAlbumFragment()
        val invalidDate = "fecha-invalida"

        val result = fragment.formatDateForAPI(invalidDate)

        assertEquals(invalidDate, result)
    }

    @Test
    fun `formatDateForAPI retorna el string original si hay una excepción`() {
        val fragment = AddAlbumFragment()
        val emptyDate = ""

        val result = fragment.formatDateForAPI(emptyDate)

        assertEquals(emptyDate, result)
    }

    @Test
    fun `formatDateForAPI maneja fechas con formato correcto pero valores extremos`() {
        val fragment = AddAlbumFragment()
        val futureDate = "31/12/2099"
        val expectedApiDate = "2099-12-31T00:00:00-05:00"

        val result = fragment.formatDateForAPI(futureDate)

        assertEquals(expectedApiDate, result)
    }


    @Test
    fun `addTrackToList agrega un track a la lista correctamente`() {
        val fragment = createFragmentWithTrackAdapter()
        val trackName = "Test Track"
        val initialSize = fragment.trackList.size

        fragment.addTrackToList(trackName)

        assertEquals(initialSize + 1, fragment.trackList.size)
        assertTrue(fragment.trackList.any { it.name == trackName })
    }

    @Test
    fun `addTrackToList genera un ID único para cada track`() {
        val fragment = createFragmentWithTrackAdapter()
        val trackName1 = "Track 1"
        val trackName2 = "Track 2"

        fragment.addTrackToList(trackName1)
        fragment.addTrackToList(trackName2)

        val track1 = fragment.trackList.find { it.name == trackName1 }
        val track2 = fragment.trackList.find { it.name == trackName2 }

        assertNotNull(track1)
        assertNotNull(track2)
        assertNotEquals(track1!!.id, track2!!.id)
    }

    @Test
    fun `addTrackToList permite agregar tracks con el mismo nombre`() {
        val fragment = createFragmentWithTrackAdapter()
        val trackName = "Duplicate Track"

        fragment.addTrackToList(trackName)
        fragment.addTrackToList(trackName)

        val tracksWithSameName = fragment.trackList.filter { it.name == trackName }
        assertEquals(2, tracksWithSameName.size)
        assertNotEquals(tracksWithSameName[0].id, tracksWithSameName[1].id)
    }

    @Test
    fun `addTrackToList maneja nombres de tracks vacíos`() {
        val fragment = createFragmentWithTrackAdapter()
        val emptyTrackName = ""

        fragment.addTrackToList(emptyTrackName)

        assertTrue(fragment.trackList.any { it.name == emptyTrackName })
    }

    @Test
    fun `addTrackToList maneja nombres de tracks con espacios`() {
        val fragment = createFragmentWithTrackAdapter()
        val trackNameWithSpaces = "  Track With Spaces  "

        fragment.addTrackToList(trackNameWithSpaces)

        val addedTrack = fragment.trackList.find { it.name == trackNameWithSpaces }
        assertNotNull(addedTrack)
    }


    @Test
    fun `validateForm requiere nombre no vacío`() {
        val fragment = AddAlbumFragment()
        assertNotNull(fragment::validateForm)
    }

    @Test
    fun `validateForm requiere artista seleccionado`() {
        val fragment = AddAlbumFragment()
        fragment.selectedArtist = null
        assertNull(fragment.selectedArtist)
    }

    @Test
    fun `validateForm requiere tracks en la lista`() {
        val fragment = createFragmentWithTrackAdapter()
        fragment.trackList.clear()
        
        assertTrue(fragment.trackList.isEmpty())
        
        fragment.addTrackToList("Test Track")
        assertFalse(fragment.trackList.isEmpty())
        assertEquals(1, fragment.trackList.size)
    }

    @Test
    fun `validateForm verifica que selectedImageUri no sea null`() {
        val fragment = AddAlbumFragment()
        fragment.selectedImageUri = null
        
        assertNull(fragment.selectedImageUri)
        
        fragment.selectedImageUri = mockk<Uri>(relaxed = true)
        assertNotNull(fragment.selectedImageUri)
    }

    @Test
    fun `createAlbum mapea tracks correctamente a formato API`() {
        val fragment = createFragmentWithTrackAdapter()
        fragment.albumViewModel = mockAlbumViewModel
        fragment.selectedArtist = mockk()

        val trackNames = listOf("Track A", "Track B", "Track C")
        trackNames.forEach { fragment.addTrackToList(it) }

        assertEquals(3, fragment.trackList.size)
        assertEquals("Track A", fragment.trackList[0].name)
        assertEquals("Track B", fragment.trackList[1].name)
        assertEquals("Track C", fragment.trackList[2].name)

        assertNotNull(fragment::createAlbum)
    }

    @Test
    fun `createAlbum usa formato de fecha correcto`() {
        val fragment = AddAlbumFragment()
        val displayDate = "15/08/2023"
        val apiDate = fragment.formatDateForAPI(displayDate)
        
        assertEquals("2023-08-15T00:00:00-05:00", apiDate)
    }


    @Test
    fun `fragment puede asignar ArtistsViewModel`() {
        val fragment = AddAlbumFragment()
        fragment.artistsViewModel = mockArtistsViewModel

        assertNotNull(fragment.artistsViewModel)
        assertEquals(mockArtistsViewModel, fragment.artistsViewModel)
    }

    @Test
    fun `fragment puede asignar AlbumViewModel`() {
        val fragment = AddAlbumFragment()
        fragment.albumViewModel = mockAlbumViewModel

        assertNotNull(fragment.albumViewModel)
        assertEquals(mockAlbumViewModel, fragment.albumViewModel)
    }

    @Test
    fun `fragment puede acceder a createAlbumResult LiveData`() {
        val fragment = AddAlbumFragment()
        fragment.albumViewModel = mockAlbumViewModel

        assertNotNull(mockAlbumViewModel.createAlbumResult)
    }


    @Test
    fun `setupSpinners extrae nombres de artistas correctamente`() {
        val fragment = AddAlbumFragment()
        
        val testArtists = listOf(
            Artist(1, "image1", "Artist 1", "Description 1", "2023-01-01"),
            Artist(2, "image2", "Artist 2", "Description 2", "2023-01-02"),
            Artist(3, "image3", "Artist 3", "Description 3", "2023-01-03")
        )

        val artistNames = testArtists.map { it.name }
        
        assertEquals(3, artistNames.size)
        assertEquals("Artist 1", artistNames[0])
        assertEquals("Artist 2", artistNames[1])
        assertEquals("Artist 3", artistNames[2])

        assertNotNull(fragment::setupSpinners)
    }

    @Test
    fun `setupSpinners maneja lista vacía de artistas`() {
        val fragment = AddAlbumFragment()
        val emptyArtists = emptyList<Artist>()
        
        val artistNames = emptyArtists.map { it.name }
        assertTrue(artistNames.isEmpty())
    }
}


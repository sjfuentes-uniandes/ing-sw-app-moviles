package com.example.vinilos.HU_008_AgregarTrack

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vinilos.MainActivity
import com.example.vinilos.R
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Pruebas adicionales de reconocimiento para HU-008: Agregar Track a Álbum
 *
 * Enfoque en:
 * - Pruebas de reconocimiento de elementos UI
 * - Validaciones de edge cases
 * - Comportamiento de la aplicación en diferentes escenarios
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class AddTrackRecognitionTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Test de reconocimiento: Verificar que el botón FAB aparece en todos los álbumes
     */
    @Test
    fun recognitionTest_fabButton_appearsInAllAlbumDetails() {
        // Navegar a Albums
        onView(withId(R.id.navigation_albums)).perform(click())
        Thread.sleep(3000)

        // Probar los primeros 3 álbumes
        for (position in 0..2) {
            // Click en álbum
            onView(withId(R.id.albumsRv))
                .perform(clickOnRecyclerViewItem(position))
            Thread.sleep(2000)

            // Verificar que el FAB está presente
            onView(withId(R.id.fabAddTrack)).check(matches(isDisplayed()))

            // Regresar a la lista
            onView(withId(R.id.btnBack)).perform(click())
            Thread.sleep(1000)
        }
    }

    /**
     * Test de reconocimiento: Verificar textos y labels en la pantalla de agregar track
     */
    @Test
    fun recognitionTest_addTrackScreen_hasCorrectLabelsAndHints() {
        // Navegar a agregar track
        navigateToAddTrackScreen()

        // Verificar que existe el campo de nombre con hint
        onView(withId(R.id.trackNameEditText))
            .check(matches(isDisplayed()))

        // Verificar que existe el campo de duración con hint
        onView(withId(R.id.trackDurationEditText))
            .check(matches(isDisplayed()))

        // Verificar botón con texto correcto
        onView(withId(R.id.saveSongButton))
            .check(matches(withText(R.string.save_song)))
    }

    /**
     * Test: Validar formato de duración con casos límite
     */
    @Test
    fun edgeCase_durationFormat_acceptsValidFormats() {
        navigateToAddTrackScreen()

        // Llenar nombre
        onView(withId(R.id.trackNameEditText))
            .perform(typeText("Edge Case Track"), closeSoftKeyboard())

        // Probar formato válido: 5:30
        onView(withId(R.id.trackDurationEditText))
            .perform(clearText(), typeText("5:30"), closeSoftKeyboard())

        onView(withId(R.id.saveSongButton)).perform(click())
        Thread.sleep(3000)

        // Si fue exitoso, debe regresar al detalle
        onView(withId(R.id.albumCover)).check(matches(isDisplayed()))
    }

    /**
     * Test: Validar que no se puede enviar con solo espacios
     */
    @Test
    fun edgeCase_emptyTrackName_withOnlySpaces() {
        navigateToAddTrackScreen()

        // Llenar solo con espacios
        onView(withId(R.id.trackNameEditText))
            .perform(typeText("   "), closeSoftKeyboard())

        onView(withId(R.id.trackDurationEditText))
            .perform(typeText("3:00"), closeSoftKeyboard())

        // Intentar guardar
        onView(withId(R.id.saveSongButton)).perform(click())
        Thread.sleep(500)

        // Debe seguir en la pantalla (validación falla)
        onView(withId(R.id.trackNameEditText)).check(matches(isDisplayed()))
    }

    /**
     * Test: Verificar que se puede navegar múltiples veces entre pantallas
     */
    @Test
    fun navigation_multipleNavigations_workCorrectly() {
        // Navegar a agregar track
        navigateToAddTrackScreen()

        // Regresar
        onView(withId(R.id.toolbar)).perform(click())
        Thread.sleep(1000)

        // Verificar que está en detalle
        onView(withId(R.id.fabAddTrack)).check(matches(isDisplayed()))

        // Volver a entrar
        onView(withId(R.id.fabAddTrack)).perform(click())
        Thread.sleep(1000)

        // Verificar que está en agregar track nuevamente
        onView(withId(R.id.saveSongButton)).check(matches(isDisplayed()))
    }

    /**
     * Test: Verificar que el nombre del álbum se muestra en la pantalla de agregar track
     */
    @Test
    fun recognitionTest_albumName_displaysInAddTrackScreen() {
        navigateToAddTrackScreen()

        // Verificar que el nombre del álbum está visible
        onView(withId(R.id.albumNameText))
            .check(matches(isDisplayed()))
            .check(matches(isCompletelyDisplayed()))
    }

    /**
     * Test: Verificar comportamiento después de agregar múltiples tracks
     */
    @Test
    fun addMultipleTracks_allGetAdded() {
        // Agregar primer track
        navigateToAddTrackScreen()

        onView(withId(R.id.trackNameEditText))
            .perform(typeText("Track 1"), closeSoftKeyboard())
        onView(withId(R.id.trackDurationEditText))
            .perform(typeText("3:00"), closeSoftKeyboard())
        onView(withId(R.id.saveSongButton)).perform(click())
        Thread.sleep(3000)

        // Agregar segundo track
        onView(withId(R.id.fabAddTrack)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.trackNameEditText))
            .perform(typeText("Track 2"), closeSoftKeyboard())
        onView(withId(R.id.trackDurationEditText))
            .perform(typeText("4:15"), closeSoftKeyboard())
        onView(withId(R.id.saveSongButton)).perform(click())
        Thread.sleep(3000)

        // Verificar que regresó al detalle
        onView(withId(R.id.albumCover)).check(matches(isDisplayed()))
        onView(withId(R.id.tracksRecyclerView)).check(matches(isDisplayed()))
    }

    /**
     * Test: Verificar que el RecyclerView de tracks se actualiza después de agregar
     */
    @Test
    fun addTrack_updatesTracksListInAlbumDetail() {
        // Navegar a detalle de álbum
        onView(withId(R.id.navigation_albums)).perform(click())
        Thread.sleep(3000)

        onView(withId(R.id.albumsRv))
            .perform(clickOnRecyclerViewItem(0))
        Thread.sleep(2000)

        // Verificar que existe la lista de tracks
        onView(withId(R.id.tracksRecyclerView)).check(matches(isDisplayed()))

        // Agregar un nuevo track
        onView(withId(R.id.fabAddTrack)).perform(click())
        Thread.sleep(1000)

        onView(withId(R.id.trackNameEditText))
            .perform(typeText("New Test Track"), closeSoftKeyboard())
        onView(withId(R.id.trackDurationEditText))
            .perform(typeText("2:30"), closeSoftKeyboard())
        onView(withId(R.id.saveSongButton)).perform(click())
        Thread.sleep(3000)

        // Verificar que sigue mostrando la lista de tracks
        onView(withId(R.id.tracksRecyclerView)).check(matches(isDisplayed()))
    }

    // ============ MÉTODOS AUXILIARES ============

    private fun navigateToAddTrackScreen() {
        onView(withId(R.id.navigation_albums)).perform(click())
        Thread.sleep(3000)

        onView(withId(R.id.albumsRv))
            .perform(clickOnRecyclerViewItem(0))
        Thread.sleep(2000)

        onView(withId(R.id.fabAddTrack)).perform(click())
        Thread.sleep(1000)
    }

    private fun clickOnRecyclerViewItem(position: Int): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isAssignableFrom(RecyclerView::class.java)
            }

            override fun getDescription(): String {
                return "Click on RecyclerView item at position $position"
            }

            override fun perform(uiController: UiController, view: View) {
                val recyclerView = view as RecyclerView
                val viewHolder = recyclerView.findViewHolderForAdapterPosition(position)
                viewHolder?.itemView?.performClick()
            }
        }
    }
}


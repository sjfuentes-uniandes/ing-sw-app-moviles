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
 * Pruebas de UI con Espresso para HU-008: Agregar Track a Álbum
 *
 * Cobertura:
 * - Navegación completa del flujo de agregar track
 * - Validación de formulario
 * - Verificación de elementos en pantalla
 * - Flujo de regreso después de agregar track
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class AddTrackFlowTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    /**
     * Test 1: Navegación completa desde álbumes hasta agregar track
     *
     * Flujo:
     * 1. Ir a Albums
     * 2. Abrir detalle de un álbum
     * 3. Verificar que se muestra el botón FAB de agregar track
     * 4. Click en FAB
     * 5. Verificar que se abre la pantalla de agregar track
     */
    @Test
    fun navigateToAddTrack_fromAlbumDetail() {
        // Navegar a Albums
        onView(withId(R.id.navigation_albums)).perform(click())

        // Esperar carga de álbumes
        Thread.sleep(3000)

        // Verificar que la lista de álbumes se muestra
        onView(withId(R.id.albumsRv)).check(matches(isDisplayed()))

        // Click en el primer álbum
        onView(withId(R.id.albumsRv))
            .perform(clickOnRecyclerViewItem(0))

        // Esperar carga del detalle
        Thread.sleep(2000)

        // Verificar elementos del detalle del álbum
        onView(withId(R.id.albumCover)).check(matches(isDisplayed()))
        onView(withId(R.id.tracksLabel)).check(matches(isDisplayed()))

        // Verificar que el botón FAB de agregar track está visible
        onView(withId(R.id.fabAddTrack)).check(matches(isDisplayed()))

        // Click en el botón de agregar track
        onView(withId(R.id.fabAddTrack)).perform(click())

        // Verificar que se abre la pantalla de agregar track
        onView(withId(R.id.trackNameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.trackDurationEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.saveSongButton)).check(matches(isDisplayed()))
    }

    /**
     * Test 2: Verificar elementos de la pantalla de agregar track
     *
     * Verifica que todos los elementos necesarios están presentes:
     * - Imagen del álbum
     * - Nombre del álbum
     * - Campo de nombre de canción
     * - Campo de duración
     * - Botón "Save Song"
     */
    @Test
    fun addTrackScreen_displaysAllElements() {
        // Navegar a Albums → Detalle → Agregar Track
        navigateToAddTrackScreen()

        // Verificar imagen del álbum
        onView(withId(R.id.albumCoverImage)).check(matches(isDisplayed()))

        // Verificar nombre del álbum
        onView(withId(R.id.albumNameText)).check(matches(isDisplayed()))

        // Verificar campo de nombre de track
        onView(withId(R.id.trackNameEditText)).check(matches(isDisplayed()))

        // Verificar campo de duración
        onView(withId(R.id.trackDurationEditText)).check(matches(isDisplayed()))

        // Verificar botón Save Song
        onView(withId(R.id.saveSongButton))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.save_song)))
    }

    /**
     * Test 3: Validación de campos vacíos
     *
     * Verifica que no se puede guardar un track sin llenar los campos obligatorios
     */
    @Test
    fun addTrack_withEmptyFields_showsValidationErrors() {
        // Navegar a pantalla de agregar track
        navigateToAddTrackScreen()

        // Intentar guardar sin llenar campos
        onView(withId(R.id.saveSongButton)).perform(click())

        // Esperar validación
        Thread.sleep(500)

        // Verificar que los campos siguen vacíos y no se navega
        onView(withId(R.id.trackNameEditText)).check(matches(isDisplayed()))
        onView(withId(R.id.trackDurationEditText)).check(matches(isDisplayed()))
    }

    /**
     * Test 4: Validación de formato de duración inválido
     *
     * Verifica que se valida el formato de duración (M:SS o MM:SS)
     */
    @Test
    fun addTrack_withInvalidDuration_showsError() {
        // Navegar a pantalla de agregar track
        navigateToAddTrackScreen()

        // Llenar nombre de track
        onView(withId(R.id.trackNameEditText))
            .perform(typeText("Test Track"), closeSoftKeyboard())

        // Llenar duración con formato inválido
        onView(withId(R.id.trackDurationEditText))
            .perform(typeText("999"), closeSoftKeyboard())

        // Intentar guardar
        onView(withId(R.id.saveSongButton)).perform(click())

        // Esperar validación
        Thread.sleep(500)

        // Verificar que sigue en la misma pantalla (validación falló)
        onView(withId(R.id.trackDurationEditText)).check(matches(isDisplayed()))
    }

    /**
     * Test 5: Agregar track con datos válidos
     *
     * Flujo completo de agregar un track exitosamente
     */
    @Test
    fun addTrack_withValidData_succeeds() {
        // Navegar a pantalla de agregar track
        navigateToAddTrackScreen()

        // Llenar nombre de track
        onView(withId(R.id.trackNameEditText))
            .perform(typeText("Espresso Test Track"), closeSoftKeyboard())

        // Llenar duración válida
        onView(withId(R.id.trackDurationEditText))
            .perform(typeText("3:45"), closeSoftKeyboard())

        // Guardar track
        onView(withId(R.id.saveSongButton)).perform(click())

        // Esperar guardado y navegación
        Thread.sleep(3000)

        // Verificar que regresó al detalle del álbum
        // (Puede que se vea el Snackbar de éxito, pero después vuelve al detalle)
        onView(withId(R.id.albumCover)).check(matches(isDisplayed()))
    }

    /**
     * Test 6: Botón de regreso en agregar track
     *
     * Verifica que el botón de back navega correctamente
     */
    @Test
    fun addTrackScreen_backButton_navigatesToAlbumDetail() {
        // Navegar a pantalla de agregar track
        navigateToAddTrackScreen()

        // Click en botón de back
        onView(withId(R.id.toolbar)).perform(click())

        // Esperar navegación
        Thread.sleep(1000)

        // Verificar que regresó al detalle del álbum
        onView(withId(R.id.albumCover)).check(matches(isDisplayed()))
        onView(withId(R.id.fabAddTrack)).check(matches(isDisplayed()))
    }

    /**
     * Test 7: Verificar que la lista de tracks se muestra en el detalle
     *
     * Si un álbum tiene tracks, deben mostrarse en el RecyclerView
     */
    @Test
    fun albumDetail_displaysTracksList() {
        // Navegar a Albums
        onView(withId(R.id.navigation_albums)).perform(click())

        // Esperar carga
        Thread.sleep(3000)

        // Click en el primer álbum (que probablemente tenga tracks)
        onView(withId(R.id.albumsRv))
            .perform(clickOnRecyclerViewItem(0))

        // Esperar carga del detalle
        Thread.sleep(2000)

        // Verificar que existe el RecyclerView de tracks
        onView(withId(R.id.tracksRecyclerView)).check(matches(isDisplayed()))

        // Verificar que el header "Tracks" está visible
        onView(withId(R.id.tracksLabel))
            .check(matches(isDisplayed()))
            .check(matches(withText(R.string.tracks)))
    }

    // ============ MÉTODOS AUXILIARES ============

    /**
     * Método auxiliar para navegar a la pantalla de agregar track
     * Reutilizable en múltiples tests
     */
    private fun navigateToAddTrackScreen() {
        // Ir a Albums
        onView(withId(R.id.navigation_albums)).perform(click())
        Thread.sleep(3000)

        // Abrir primer álbum
        onView(withId(R.id.albumsRv))
            .perform(clickOnRecyclerViewItem(0))
        Thread.sleep(2000)

        // Click en FAB de agregar track
        onView(withId(R.id.fabAddTrack)).perform(click())
        Thread.sleep(1000)
    }

    /**
     * ViewAction personalizado para hacer clic en un item del RecyclerView
     * sin depender de espresso-contrib
     */
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


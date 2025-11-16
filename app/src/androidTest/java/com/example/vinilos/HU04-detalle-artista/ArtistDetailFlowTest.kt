package com.example.vinilos.HU04_detalle_artista

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
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

@LargeTest
@RunWith(AndroidJUnit4::class)
class ArtistDetailFlowTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigateToArtistDetail_and_backToList() {
        // Ir al tab de Artists por ID (sin depender de contentDescription)
        onView(withId(R.id.navigation_artists)).perform(click())

        // Espera breve por la carga de la lista (backend puede estar en cold start)
        Thread.sleep(3000)

        // Verificar que la lista se ve
        onView(withId(R.id.artists_recycler_view)).check(matches(isDisplayed()))

        // Click en el primer artista de la lista usando ViewAction personalizado
        onView(withId(R.id.artists_recycler_view))
            .perform(clickOnRecyclerViewItem(0))

        // Verificar que se muestra el detalle (imagen y títulos)
        onView(withId(R.id.artistImage)).check(matches(isDisplayed()))
        onView(withId(R.id.artistName)).check(matches(isDisplayed()))
        onView(withId(R.id.descriptionLabel)).check(matches(withText(R.string.description)))

        // Pulsar el botón de back del detalle
        onView(withId(R.id.btnBack)).perform(click())

        // Validar que regresa a la lista
        onView(withId(R.id.artists_recycler_view)).check(matches(isDisplayed()))
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

    @Test
    fun navigateHomeThenArtists_alwaysShowsArtistsList() {
        // Ir a Home
        onView(withId(R.id.navigation_home)).perform(click())

        // Ir al tab de Artists
        onView(withId(R.id.navigation_artists)).perform(click())

        // Validar que muestra la lista (no un detalle residual)
        onView(withId(R.id.artists_recycler_view)).check(matches(isDisplayed()))
    }
}

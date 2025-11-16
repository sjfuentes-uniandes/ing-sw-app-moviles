package com.example.vinilos.HU02_detalle_album

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
class AlbumDetailFlowTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigateToAlbumDetail_and_backToList() {
        // Navigate to Albums tab
        onView(withId(R.id.navigation_albums)).perform(click())

        // Wait for albums list to load (backend may be in cold start)
        Thread.sleep(3000)

        // Verify albums list is displayed
        onView(withId(R.id.albumsRv)).check(matches(isDisplayed()))

        // Click on the first album in the list
        onView(withId(R.id.albumsRv))
            .perform(clickOnRecyclerViewItem(0))

        // Verify album detail elements are displayed
        onView(withId(R.id.albumCover)).check(matches(isDisplayed()))
        onView(withId(R.id.albumName)).check(matches(isDisplayed()))
        onView(withId(R.id.descriptionLabel)).check(matches(withText(R.string.description)))

        // Click back button
        onView(withId(R.id.btnBack)).perform(click())

        // Verify we're back at albums list
        onView(withId(R.id.albumsRv)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateHomeToAlbums_alwaysShowsAlbumsList() {
        // Navigate to Home
        onView(withId(R.id.navigation_home)).perform(click())

        // Navigate to Albums
        onView(withId(R.id.navigation_albums)).perform(click())

        // Wait for albums list to load
        Thread.sleep(3000)

        // Verify albums list is displayed (not a detail residual)
        onView(withId(R.id.albumsRv)).check(matches(isDisplayed()))
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

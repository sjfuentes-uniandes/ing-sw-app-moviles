package com.example.vinilos.HU06_detalle_colleccionista

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
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anyOf
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class CollectorDetailFlowTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigateToCollectorDetail_and_backToList() {
        // Navigate to Collectors tab
        onView(withId(R.id.navigation_collector)).perform(click())

        // Wait for collectors list to load
        Thread.sleep(3000)

        // Verify collectors list is displayed
        onView(withId(R.id.collectorsRv)).check(matches(isDisplayed()))

        // Click on the first collector in the list
        onView(withId(R.id.collectorsRv))
            .perform(clickOnRecyclerViewItem(0))

        // Wait for collectors detail information to load.
        Thread.sleep(2000)

        // Verify collector detail elements are displayed
        onView(withId(R.id.collectorTitle)).check(matches(isDisplayed()))
        onView(withId(R.id.collectorName)).check(matches(isDisplayed()))
        onView(withId(R.id.collectorTelephone)).check(matches(isDisplayed()))
        onView(withId(R.id.collectorEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.albumsContainer)).check(matches(isDisplayed()))

        // Verify collector detail label elements
        onView(withId(R.id.collectorNameLabel)).check(matches(withText(R.string.collector_name)))
        onView(withId(R.id.collectorTelephoneLabel)).check(matches(withText(R.string.telephone)))
        onView(withId(R.id.collectorEmailLabel)).check(matches(withText(R.string.email)))
        onView(withId(R.id.albumsLabel)).check(matches(withText(R.string.albums)))

        // Check for album names or the "no albums" message
        onView(
            anyOf(
                allOf(
                    withText("Este coleccionista no tiene albumes registrados"),
                    withParent(withId(R.id.albumsContainer))
                ),
                allOf(
                    withText(Matchers.startsWith("• ")),
                    withParent(withId(R.id.albumsContainer))
                )
            )
        ).check(matches(isDisplayed()))

        // Click back button
        onView(withId(R.id.btnBack)).perform(click())

        // Verify navigation to collectors list
        onView(withId(R.id.collectorsRv)).check(matches(isDisplayed()))
    }

    @Test
    fun navigateHomeToCollectors_alwaysShowsCollectorsList() {
        // Navigate to Home
        onView(withId(R.id.navigation_home)).perform(click())

        // Navigate to collectors
        onView(withId(R.id.navigation_collector)).perform(click())

        // Wait for collectors list to load
        Thread.sleep(3000)

        // Verify collectors list is displayed (not a detail residual)
        onView(withId(R.id.collectorsRv)).check(matches(isDisplayed()))
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

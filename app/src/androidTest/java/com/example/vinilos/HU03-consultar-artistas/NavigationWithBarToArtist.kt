package com.example.vinilos

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class NavigationWithBarToArtist {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun navigationWithBar() {
        checkHeaderTitle("Homepage")

        clickBottomNavItem(R.id.navigation_artists, "Artists")

        checkHeaderTitle("Artists")

        clickBottomNavItem(R.id.navigation_home, "Home")

        checkHeaderTitle("Homepage")
    }

    private fun checkHeaderTitle(title: String) {
        onView(
            allOf(
                withText(title),
                withParent(withId(R.id.header_layout))
            )
        ).check(matches(isDisplayed()))
    }

    private fun clickBottomNavItem(id: Int, description: String) {
        onView(
            allOf(
                withId(id),
                withContentDescription(description)
            )
        ).perform(click())
    }

}
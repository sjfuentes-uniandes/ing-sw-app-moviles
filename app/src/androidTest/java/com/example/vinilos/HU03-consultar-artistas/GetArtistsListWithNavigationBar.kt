package com.example.vinilos

import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.espresso.matcher.ViewMatchers.hasMinimumChildCount
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GetArtistsListWithNavigationBar {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun getArtistsListWithNavigationBar() {
        val frameLayout = onView(
            allOf(
                withId(R.id.navigation_artists),
                withContentDescription("Artists"),
            )
        ).perform(click())

        Thread.sleep(3000)

        val recyclerView = onView(withId(R.id.artists_recycler_view))
        recyclerView.check(matches(isDisplayed()))
        Thread.sleep(3000)

        onView(withId(R.id.artists_recycler_view))
            .check(matches(hasMinimumChildCount(1)))
    }

}
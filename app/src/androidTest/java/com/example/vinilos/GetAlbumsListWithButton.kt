package com.example.vinilos


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withClassName
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
import org.hamcrest.Matchers.`is`
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class GetAlbumsListWithButton {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun albums_button() {
        val textView = onView(
            allOf(
                withId(R.id.btn_albums), withText("Albums"),
                withParent(withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Albums")))

        val materialTextView = onView(
            allOf(
                withId(R.id.btn_albums), withText("Albums"),
                childAtPosition(
                    childAtPosition(
                        withClassName(`is`("androidx.constraintlayout.widget.ConstraintLayout")),
                        1
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        materialTextView.perform(click())

        // Wait for RecyclerView to be displayed
        Thread.sleep(2000)
        
        val recyclerView = onView(withId(R.id.albumsRv))
        recyclerView.check(matches(isDisplayed()))

        // Check if albums are loaded from backend
        Thread.sleep(3000)
        
        // Verify RecyclerView has at least one item
        onView(withId(R.id.albumsRv))
            .check(matches(hasMinimumChildCount(1)))

        val textView4 = onView(
            allOf(
                withText("Albums"),
                withParent(
                    allOf(
                        withId(R.id.header_layout),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView4.check(matches(withText("Albums")))

        val frameLayout = onView(
            allOf(
                withId(R.id.navigation_albums), withContentDescription("Albums"),
                withParent(withParent(withId(R.id.nav_view))),
                isDisplayed()
            )
        )
        frameLayout.check(matches(isDisplayed()))
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}

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
class GetCollectorsListWithBar {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun getCollectorsListWithBar() {
        val frameLayout = onView(
            allOf(
                withId(R.id.navigation_collector), withContentDescription("Collectors"),
                withParent(withParent(withId(R.id.nav_view))),
                isDisplayed()
            )
        )
        frameLayout.check(matches(isDisplayed()))

        val bottomNavigationItemView = onView(
            allOf(
                withId(R.id.navigation_collector), withContentDescription("Collectors"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.nav_view),
                        0
                    ),
                    3
                ),
                isDisplayed()
            )
        )
        bottomNavigationItemView.perform(click())

        // Wait for navigation and backend data loading
        Thread.sleep(3000)

        val textView = onView(
            allOf(
                withText("Collectors"),
                withParent(
                    allOf(
                        withId(R.id.header_layout),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        textView.check(matches(withText("Collectors")))

        // Verify RecyclerView is displayed and has items from backend
        val recyclerView = onView(withId(R.id.collectorsRv))
        recyclerView.check(matches(isDisplayed()))
        


        val textView2 = onView(
            allOf(
                withText("Jaime Andrés Monsalve"),
                withParent(withParent(withId(R.id.collectorsRv))),
                isDisplayed()
            )
        )
        textView2.check(matches(withText("Jaime Andrés Monsalve")))

        val textView3 = onView(
            allOf(
                withText("j.monsalve@gmail.com"),
                withParent(withParent(withId(R.id.collectorsRv))),
                isDisplayed()
            )
        )
        textView3.check(matches(withText("j.monsalve@gmail.com")))
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

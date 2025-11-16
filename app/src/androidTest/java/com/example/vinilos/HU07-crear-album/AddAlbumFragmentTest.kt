package com.example.vinilos

import android.app.Activity
import android.app.Instrumentation
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.test.espresso.Espresso.closeSoftKeyboard
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.action.ViewActions.scrollTo
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.Intents.intending
import androidx.test.espresso.intent.matcher.IntentMatchers.hasAction
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.example.vinilos.HU07.DisableAnimationsRule
import com.example.vinilos.ui.albums.AddAlbumFragment
import org.hamcrest.Matcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class AddAlbumFragmentTest {

    @Rule
    @JvmField
    var activityRule = ActivityScenarioRule(MainActivity::class.java)

    @get:Rule
    val disableAnimationsRule = DisableAnimationsRule()

    @Test
    fun createAlbum_fillsFormAndSubmits_navigatesBack() {

        onView(withId(R.id.btn_albums)).check(matches(isDisplayed()))
        closeSoftKeyboard()
        onView(withId(R.id.btn_albums)).perform(forceClick())

        Thread.sleep(2000)

        onView(withId(R.id.add_album_button)).check(matches(isDisplayed()))
        closeSoftKeyboard()
        onView(withId(R.id.add_album_button)).perform(forceClick())

        Thread.sleep(2000)

        onView(withText("Crear Album")).check(matches(isDisplayed()))

        onView(withId(R.id.name_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.name_edit_text)).perform(forceClick())
        onView(withId(R.id.name_edit_text)).perform(replaceText("Nuevo Álbum de Prueba"))
        closeSoftKeyboard()

        onView(withId(R.id.description_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.description_edit_text)).perform(forceClick())
        onView(withId(R.id.description_edit_text)).perform(replaceText("Descripción de prueba para el nuevo álbum."))
        closeSoftKeyboard()

        onView(withId(R.id.release_date_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.release_date_edit_text)).perform(replaceText("2024-01-01"))
        closeSoftKeyboard()

        activityRule.scenario.onActivity { activity ->
            val navHost = (activity as MainActivity)
                .supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_activity_main)
            val current = navHost?.childFragmentManager?.fragments?.firstOrNull { it is AddAlbumFragment } as? AddAlbumFragment
            current?.let { frag ->
                val firstArtist = frag.artistsViewModel.artist.value?.firstOrNull()
                if (firstArtist != null) {
                    frag.selectedArtist = firstArtist
                    frag.binding.artistAutoComplete.setText(firstArtist.name, false)
                }
            }
        }

        onView(withId(R.id.genre_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.genre_edit_text)).perform(forceClick())
        Thread.sleep(200)
        onView(withId(R.id.genre_edit_text)).perform(replaceText("Rock"))


        onView(withId(R.id.record_label_edit_text)).check(matches(isDisplayed()))
        onView(withId(R.id.record_label_edit_text)).perform(forceClick())
        Thread.sleep(200)
        onView(withId(R.id.record_label_edit_text)).perform(replaceText("Sony Music"))

        Intents.init()
        val dummyImageUri = Uri.parse("content://com.example.vinilos.test/placeholder.jpg")
        val resultData = Intent().setData(dummyImageUri)
        val result = Instrumentation.ActivityResult(Activity.RESULT_OK, resultData)
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result)
        onView(withId(R.id.browse_image_button)).perform(forceClick())

        activityRule.scenario.onActivity { activity ->
            val navHost = (activity as MainActivity)
                .supportFragmentManager
                .findFragmentById(R.id.nav_host_fragment_activity_main)
            val current = navHost?.childFragmentManager?.fragments?.firstOrNull { it is AddAlbumFragment } as? AddAlbumFragment
            current?.addTrackToList("Demo Track A")
        }

         onView(withId(R.id.create_album_button)).perform(scrollTo())
         onView(withId(R.id.create_album_button)).check(matches(isDisplayed()))
         onView(withId(R.id.create_album_button)).perform(forceClick())
        Intents.release()

        Thread.sleep(1500)
        onView(withId(R.id.add_album_button)).check(matches(isDisplayed()))
    }

    private fun forceClick(): ViewAction {
        return object : ViewAction {
            override fun getConstraints(): Matcher<View> {
                return isDisplayed()
            }

            override fun getDescription(): String {
                return "force click using view.performClick()"
            }

            override fun perform(uiController: UiController?, view: View) {
                view.performClick()
                uiController?.loopMainThreadUntilIdle()
            }
        }
    }

    private fun clickFirstAvailable(vararg matchers: Matcher<View>) {
        for (m in matchers) {
            try {
                onView(m).check(matches(isDisplayed())).perform(click())
                return
            } catch (_: Throwable) {
                // probar siguiente
            }
        }
        throw AssertionError("No se encontró botón de confirmación del date picker")
    }
}
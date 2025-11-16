package com.example.vinilos.HU07
import android.os.Build
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.uiautomator.UiDevice
import org.junit.rules.TestRule
import org.junit.runner.Description
import org.junit.runners.model.Statement
import java.io.IOException


class DisableAnimationsRule:TestRule {
    override fun apply(base: Statement, description: Description): Statement {
        return object : Statement() {
            override fun evaluate() {
                disableAnimations()
                try {
                    base.evaluate()
                } finally {
                }
            }
        }
    }

    private fun disableAnimations() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        try {
            device.executeShellCommand("settings put global window_animation_scale 0")
            device.executeShellCommand("settings put global transition_animation_scale 0")
            device.executeShellCommand("settings put global animator_duration_scale 0")
        } catch (e: IOException) {
            System.err.println("Error al desactivar animaciones: ${e.message}")
        }
    }

    private fun enableAnimations() {
        val instrumentation = InstrumentationRegistry.getInstrumentation()
        val device = UiDevice.getInstance(instrumentation)

        try {
            device.executeShellCommand("settings put global window_animation_scale 1")
            device.executeShellCommand("settings put global transition_animation_scale 1")
            device.executeShellCommand("settings put global animator_duration_scale 1")
        } catch (e: IOException) {
            System.err.println("Error al reactivar animaciones: ${e.message}")
        }
    }
}
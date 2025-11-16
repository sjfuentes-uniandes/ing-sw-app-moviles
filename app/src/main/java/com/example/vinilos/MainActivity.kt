package com.example.vinilos

import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.example.vinilos.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the navigation host fragment from this Activity
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment_activity_main) as NavHostFragment
        // Instantiate the navController using the NavHostFragment
        navController = navHostFragment.navController
        // Make sure actions in the ActionBar get propagated to the NavController
        Log.d("act", navController.toString())

        val navView: BottomNavigationView = binding.navView

        // Configurar el listener manualmente para controlar la navegación
        navView.setOnItemSelectedListener { item ->
            val navOptions = NavOptions.Builder()
                .setLaunchSingleTop(true)
                .setRestoreState(false)
                .setPopUpTo(R.id.mobile_navigation, false)
                .build()

            when (item.itemId) {
                R.id.navigation_home -> {
                    if (navController.currentDestination?.id != R.id.navigation_home) {
                        navController.navigate(R.id.navigation_home, null, navOptions)
                    }
                    true
                }
                R.id.navigation_albums -> {
                    if (navController.currentDestination?.id != R.id.navigation_albums) {
                        navController.navigate(R.id.navigation_albums, null, navOptions)
                    }
                    true
                }
                R.id.navigation_artists -> {
                    if (navController.currentDestination?.id != R.id.navigation_artists) {
                        navController.navigate(R.id.navigation_artists, null, navOptions)
                    }
                    true
                }
                R.id.navigation_collector -> {
                    if (navController.currentDestination?.id != R.id.navigation_collector) {
                        navController.navigate(R.id.navigation_collector, null, navOptions)
                    }
                    true
                }
                R.id.navigation_user -> {
                    if (navController.currentDestination?.id != R.id.navigation_user) {
                        navController.navigate(R.id.navigation_user, null, navOptions)
                    }
                    true
                }
                else -> false
            }
        }
    }
}
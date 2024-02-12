package com.dicoding.navigations

import android.os.Bundle
import android.view.Menu
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportActionBar?.hide()

        val navView: BottomNavigationView = findViewById(R.id.bottomNavigationView)

        val navController = findNavController(R.id.nav_host_fragment)

        // TODO: 8. Introduce about BottomNavigationView and its configuration setup
        val appBarConfiguration = AppBarConfiguration.Builder(
            R.id.homeFragment, R.id.myTripsFragment, R.id.aboutUsFragment
        ).build()

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithCustomNavController(navController)

    }
}

// Handling Bug of ButtomNavigationView Navigation with Deeplink
// Source: https://github.com/android/architecture-components-samples/issues/1003
fun BottomNavigationView.setupWithCustomNavController(navController: NavController?) {
    navController?.let {
        this.setupWithNavController(it)
    }
    this.setOnItemSelectedListener { menuItem ->
        val builder = NavOptions.Builder().setLaunchSingleTop(true).setRestoreState(false)
        val graph = navController?.currentDestination?.parent
        val destination = graph?.findNode(menuItem.itemId)
        if (menuItem.order and Menu.CATEGORY_SECONDARY == 0) {
            navController?.graph?.findStartDestination()?.id?.let {
                builder.setPopUpTo(
                    it,
                    inclusive = false,
                    saveState = true
                )
            }
        }
        val options = builder.build()
        destination?.id?.let { id -> navController.navigate(id, null, options) }
        return@setOnItemSelectedListener true
    }
}
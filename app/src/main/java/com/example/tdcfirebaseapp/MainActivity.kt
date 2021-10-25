package com.example.tdcfirebaseapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.tdcfirebaseapp.databinding.ActivityMainBinding
import com.example.tdcfirebaseapp.pages.auth.login.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val layout = R.id.nav_host_fragment_activity_main

    override fun onCreate(savedInstanceState: Bundle?) {
        checkForUserLoggedIn()

        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupDrawerLayout()
    }

    private fun setupDrawerLayout() {
        val navigationView = binding.navigationView
        val toolbar = binding.topAppBar

        navController = findNavController(layout)
        drawerLayout = binding.drawerLayout
        navigationView.setupWithNavController(navController)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_tasks,
                R.id.navigation_done_tasks,
                R.id.navigation_profile
            ),
            drawerLayout
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)
        navigationView.setCheckedItem(R.id.navigation_tasks)
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(layout).navigateUp(appBarConfiguration)
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    private fun checkForUserLoggedIn() {
        val isUserLoggedIn = false

        if (!isUserLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)

            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode != RESULT_OK) {
                    finish()
                }
            }.launch(loginIntent)
        }
    }
}
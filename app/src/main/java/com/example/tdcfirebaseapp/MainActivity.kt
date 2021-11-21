package com.example.tdcfirebaseapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.example.tdcfirebaseapp.databinding.ActivityMainBinding
import com.example.tdcfirebaseapp.pages.auth.login.LoginActivity
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import org.w3c.dom.Text

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
                R.id.navigation_unfinished_tasks,
                R.id.navigation_finished_tasks,
                R.id.navigation_profile
            ),
            drawerLayout
        )

        toolbar.setupWithNavController(navController, appBarConfiguration)
        navigationView.setCheckedItem(R.id.navigation_tasks)

        setupNavHeaderInfo(navigationView)
    }

    private fun setupNavHeaderInfo(navigationView: NavigationView) {
        val headerView = navigationView.getHeaderView(0)
        val userNameView = headerView.findViewById<TextView>(R.id.user_name_header)
        val userEmailView = headerView.findViewById<TextView>(R.id.user_email_header)

        val fbUser = FirebaseAuth.getInstance().currentUser

        if (fbUser != null) {
            userNameView.text = if (fbUser.displayName.isNullOrBlank()) {
                getString(R.string.anonimous_string)
            } else fbUser.displayName

            userEmailView.text = fbUser.email
            userEmailView.visibility = View.VISIBLE
            userNameView.visibility = View.VISIBLE
        } else {
            userEmailView.visibility = View.GONE
            userNameView.visibility = View.GONE
        }
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
        val isUserLoggedIn = FirebaseAuth.getInstance().currentUser != null

        if (!isUserLoggedIn) {
            val loginIntent = Intent(this, LoginActivity::class.java)

            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode != RESULT_OK) {
                    finish()
                }
            }.launch(loginIntent)
        }
    }

    fun requestAppReload() {
        val newIntent = Intent(this, MainActivity::class.java)
        navigateUpTo(newIntent)
        startActivity(intent)
    }
}
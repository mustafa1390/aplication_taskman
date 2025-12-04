package com.example.aplication_aplication_taskman

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
 
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupNavigation(
            drawerId = R.id.drawer_layout,
            navViewId = R.id.nav_view,
            toolbarId = R.id.toolbar
        )

        // show token debug toast: display token value if present, otherwise indicate missing
        val tokenManager = TokenManager(this)
        val token = tokenManager.getToken()
        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "No token stored", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Token: $token", Toast.LENGTH_SHORT).show()
        }

        // Setup bottom navigation
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    true
                }
                R.id.nav_listtask -> {
                    startActivity(Intent(this, TaskActivity::class.java))
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.nav_about -> {
                    startActivity(Intent(this, AboutActivity::class.java))
                    true
                }
                else -> false
            }
        }

     }
 
}

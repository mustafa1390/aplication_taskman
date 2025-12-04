package com.example.aplication_aplication_taskman

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.navigation.NavigationView

open class BaseActivity : AppCompatActivity() {

    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navView: NavigationView
    private lateinit var toggle: ActionBarDrawerToggle

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun setupNavigation(drawerId: Int, navViewId: Int, toolbarId: Int) {
        drawerLayout = findViewById(drawerId)
        navView = findViewById(navViewId)
        val toolbar: androidx.appcompat.widget.Toolbar = findViewById(toolbarId)
        setSupportActionBar(toolbar)

        toggle = ActionBarDrawerToggle(
            this, drawerLayout, toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        navView.setNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            drawerLayout.closeDrawers()

            when (menuItem.itemId) {
                R.id.nav_home -> {
                    if (this !is MainActivity) {
                        startActivity(Intent(this, MainActivity::class.java))
                    }
                }
                R.id.nav_profile -> {
                    if (this !is ProfileActivity) {
                        startActivity(Intent(this, ProfileActivity::class.java))
                    }
                }
                R.id.nav_login -> {
                    if (this !is LoginActivity) {
                        startActivity(Intent(this, LoginActivity::class.java))
                    }
                }
                R.id.nav_register -> {
                    if (this !is RegisterActivity) {
                        startActivity(Intent(this, RegisterActivity::class.java))
                    }
                }
                R.id.nav_item -> {
                    if (this !is RegisterActivity) {
                        startActivity(Intent(this, DashboardActivity::class.java))
                    }
                }
                R.id.nav_codeverify -> {
                    if (this !is RegisterActivity) {
                        startActivity(Intent(this, CodeverifyActivity::class.java))
                    }
                }
//                R.id.nav_settings -> {
//                    if (this !is SettingsActivity) {
//                        startActivity(Intent(this, SettingsActivity::class.java))
//                    }
//                }
                R.id.nav_about -> {
                    if (this !is AboutActivity) {
                        startActivity(Intent(this, AboutActivity::class.java))
                    }
                }
                R.id.nav_blog -> {
                    if (this !is BlogActivity) {
                        startActivity(Intent(this, BlogActivity::class.java))
                    }
                }
                R.id.nav_singlecard -> {
                    if (this !is BlogActivity) {
                        startActivity(Intent(this, CardActivity::class.java))
                    }
                }
                R.id.nav_listblog -> {
                    if (this !is ListblogActivity) {
                        startActivity(Intent(this, ListblogActivity::class.java))
                    }
                }
                R.id.nav_editprofile -> {
                    if (this !is ListblogActivity) {
                        startActivity(Intent(this, EditprofileActivity::class.java))
                    }
                }
                R.id.nav_listtask -> {
                    if (this !is ListblogActivity) {
                        startActivity(Intent(this, TaskActivity::class.java))
                    }
                }
            }
            true
        }
    }
}

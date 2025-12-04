package com.example.aplication_aplication_taskman

import android.os.Bundle


class SettingsActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        setupNavigation(
            drawerId = R.id.drawer_layout,
            navViewId = R.id.nav_view,
            toolbarId = R.id.toolbar
        )
    }
}
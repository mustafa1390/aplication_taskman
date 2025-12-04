package com.example.aplication_aplication_taskman

import android.os.Bundle


class BlogActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blog)

        setupNavigation(
            drawerId = R.id.drawer_layout,
            navViewId = R.id.nav_view,
            toolbarId = R.id.toolbar
        )
    }
}
package com.example.aplication_aplication_taskman

import android.os.Bundle
import android.widget.Toast

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
    }
}

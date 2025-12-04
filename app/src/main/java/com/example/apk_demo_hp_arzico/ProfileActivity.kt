package com.example.aplication_aplication_taskman

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ProfileActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val tv = findViewById<TextView>(R.id.tvProfile)
        fetchProfile(tv)


        setupNavigation(
            drawerId = R.id.drawer_layout,
            navViewId = R.id.nav_view,
            toolbarId = R.id.toolbar
        )

    }

    private fun fetchProfile(tv: TextView) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val tokenManager = TokenManager(this@ProfileActivity)
                val token = tokenManager.getToken()
                if (token.isNullOrEmpty()) {
                    runOnUiThread { tv.text = "No token stored" }
                    return@launch
                }

                val svc = ApiClient.service(this@ProfileActivity)
                val resp = svc.getProfile()

                if (resp.isSuccessful) {
                    val profileWrapper = resp.body()
                    val data = profileWrapper?.data
                    runOnUiThread {
                        if (data != null) {
                            tv.text = "ID: ${data.id}\nName: ${data.name}\nEmail: ${data.email}"
                        } else {
                            tv.text = "Profile data missing"
                        }
                    }
                } else {
                    val err = resp.errorBody()?.string() ?: "Failed"
                    runOnUiThread { tv.text = "Error: $err" }
                }
            } catch (e: Exception) {
                runOnUiThread { tv.text = "Exception: ${e.message}" }
            }
        }
    }
}
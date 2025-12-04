package com.example.aplication_aplication_taskman

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import com.google.android.material.textfield.TextInputLayout
import com.google.android.material.textfield.TextInputEditText
class RegisterActivity : AppCompatActivity() {

    private lateinit var tokenManager: TokenManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        val tv_title = findViewById<TextView>(R.id.tv_title)
//        val textEmail = findViewById<TextInputLayout>(R.id.textEmail)
//        val etTell = findViewById<TextInputEditText>(R.id.etTell)
        val etFullname = findViewById<EditText>(R.id.etFullname)
        val etTell = findViewById<EditText>(R.id.etTell)
        val etPass = findViewById<EditText>(R.id.etPassword)
        val btnLogin = findViewById<Button>(R.id.btnLogin)
        val nav_login = findViewById<Button>(R.id.nav_login)

        tokenManager = TokenManager(this)

        runOnUiThread {
            Toast.makeText(
                this@RegisterActivity,
                "ToastAlert: ${tokenManager.getToken()} ",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnLogin.setOnClickListener {
            val name = etFullname.text.toString().trim()
            val tell = etTell.text.toString().trim()
            val password = etPass.text.toString().trim()

            if (tell.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter credentials", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 🔥 Launch background thread (IO) for API call
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val service = ApiClient.service(this@RegisterActivity)
                    val response = service.login(RegisterRequest(name, tell, password))

                    if (response.isSuccessful) {
                        val body = response.body()

                        // <-- changed: read token from body.data.token
                        val token = body?.data?.token
                        if (!token.isNullOrEmpty()) {

                            // save bearer token
                            tokenManager.saveToken(token)

                            runOnUiThread {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "SUCCESS ${token}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }

                            // <-- changed: open ProfileActivity after login
                            runOnUiThread {
                                startActivity(Intent(this@RegisterActivity, ProfileActivity::class.java))
                                finish()
                            }

                        } else {
                            runOnUiThread {
                                Toast.makeText(
                                    this@RegisterActivity,
                                    "No token found in response",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@RegisterActivity,
                                "Login failed: ${response.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                } catch (e: Exception) {
                    runOnUiThread {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Error: ${e.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

        nav_login.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }
    }
}

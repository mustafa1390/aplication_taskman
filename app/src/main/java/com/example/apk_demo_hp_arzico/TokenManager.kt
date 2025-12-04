package com.example.aplication_aplication_taskman

import android.content.Context

class TokenManager(private val context: Context) {
    private val prefs = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveToken(token: String) {
        prefs.edit().putString("bearer_token", token).apply()
    }

    fun clearToken() {
        prefs.edit().remove("bearer_token").apply()
    }

    fun getToken(): String? = prefs.getString("bearer_token", null)
}
package com.example.aplication_aplication_taskman

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputEditText

class EditprofileActivity : AppCompatActivity() {

    private lateinit var etFullName: TextInputEditText
    private lateinit var etEmail: TextInputEditText
    private lateinit var etPhone: TextInputEditText
    private lateinit var etBio: TextInputEditText
    private lateinit var ivProfileAvatar: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editprofile)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // Initialize views
        etFullName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPhone = findViewById(R.id.etPhone)
        etBio = findViewById(R.id.etBio)
        ivProfileAvatar = findViewById(R.id.ivProfileAvatar)

        // Load current user profile data (sample data)
        loadProfileData()

        // Button listeners
        val btnChangeAvatar = findViewById<Button>(R.id.btnChangeAvatar)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        btnChangeAvatar.setOnClickListener {
            Toast.makeText(this, "تصویر پروفایل را انتخاب کنید", Toast.LENGTH_SHORT).show()
            // TODO: Launch image picker (e.g., gallery or camera)
        }

        btnSave.setOnClickListener {
            saveProfile()
        }

        btnCancel.setOnClickListener {
            finish()
        }

        setupFAB()
    }

    private fun setupFAB() {
        val fab = findViewById<FloatingActionButton>(R.id.fabMenu)
        fab.setOnClickListener {
            Toast.makeText(this, "Share Profile", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadProfileData() {
        // Sample user data (in a real app, this would come from SharedPreferences, database, or API)
        etFullName.setText("علی احمدی")
        etEmail.setText("ali.ahmadi@example.com")
        etPhone.setText("09123456789")
        etBio.setText("من یک کاربر شبکه بانکی ارزی کو هستم. دوست دارم از خدمات دیجیتالی استفاده کنم.")
    }

    private fun saveProfile() {
        val fullName = etFullName.text.toString().trim()
        val email = etEmail.text.toString().trim()
        val phone = etPhone.text.toString().trim()
        val bio = etBio.text.toString().trim()

        // Validation
        if (fullName.isEmpty()) {
            Toast.makeText(this, "لطفاً نام کامل را وارد کنید", Toast.LENGTH_SHORT).show()
            return
        }

        if (email.isEmpty()) {
            Toast.makeText(this, "لطفاً ایمیل را وارد کنید", Toast.LENGTH_SHORT).show()
            return
        }

        if (phone.isEmpty()) {
            Toast.makeText(this, "لطفاً شماره تلفن را وارد کنید", Toast.LENGTH_SHORT).show()
            return
        }

        // TODO: Send update request to backend API
        val profileData = mapOf(
            "fullName" to fullName,
            "email" to email,
            "phone" to phone,
            "bio" to bio
        )

        Toast.makeText(this, "پروفایل با موفقیت به‌روز شد", Toast.LENGTH_SHORT).show()

        // Close the activity after save
        finish()
    }
}

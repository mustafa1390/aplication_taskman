package com.example.aplication_aplication_taskman

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class CardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)

        // Card data (in a real app, this would come from an intent or database)
        val cardholderName = "علی احمدی"
        val cardNumber = "6221061234561234"
        val expiryDate = "12/26"
        val cvv = "123"

        // Front card display
        val tvCardNumber = findViewById<TextView>(R.id.tvCardNumber)
        val tvCardholderName = findViewById<TextView>(R.id.tvCardholderName)
        val tvExpiryDate = findViewById<TextView>(R.id.tvExpiryDate)

        // Details section
        val tvDetailsName = findViewById<TextView>(R.id.tvDetailsName)
        val tvDetailsNumber = findViewById<TextView>(R.id.tvDetailsNumber)
        val tvDetailsExpiry = findViewById<TextView>(R.id.tvDetailsExpiry)
        val tvDetailsCvv = findViewById<TextView>(R.id.tvDetailsCvv)

        // Set card data
        tvCardNumber.text = maskCardNumber(cardNumber)
        tvCardholderName.text = cardholderName
        tvExpiryDate.text = expiryDate

        tvDetailsName.text = cardholderName
        tvDetailsNumber.text = formatCardNumber(cardNumber)
        tvDetailsExpiry.text = expiryDate
        tvDetailsCvv.text = "•••" // Always masked

        // Action buttons
        val btnEdit = findViewById<Button>(R.id.btnEdit)
        val btnDelete = findViewById<Button>(R.id.btnDelete)

        btnEdit.setOnClickListener {
            Toast.makeText(this, "ویرایش کارت", Toast.LENGTH_SHORT).show()
            // TODO: Launch edit card activity or dialog
        }

        btnDelete.setOnClickListener {
            Toast.makeText(this, "حذف کارت", Toast.LENGTH_SHORT).show()
            // TODO: Confirm and delete card from backend
        }
    }

    private fun maskCardNumber(cardNumber: String): String {
        return if (cardNumber.length >= 4) {
            val lastFour = cardNumber.takeLast(4)
            "•••• •••• •••• $lastFour"
        } else {
            cardNumber
        }
    }

    private fun formatCardNumber(cardNumber: String): String {
        return if (cardNumber.length == 16) {
            "${cardNumber.substring(0, 4)} ${cardNumber.substring(4, 8)} ${cardNumber.substring(8, 12)} ${cardNumber.substring(12, 16)}"
        } else {
            cardNumber
        }
    }
}

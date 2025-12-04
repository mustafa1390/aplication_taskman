package com.example.aplication_aplication_taskman

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar

class DashboardActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        // feature icons - example IDs from layout
        val iconStore = findViewById<LinearLayout>(R.id.icon_store)
        val iconBalance = findViewById<LinearLayout>(R.id.icon_balance)
        val iconMyReceipts = findViewById<LinearLayout>(R.id.icon_my_receipts)

        iconStore.setOnClickListener {
            Toast.makeText(this, "فروشگاه", Toast.LENGTH_SHORT).show()
        }

        iconBalance.setOnClickListener {
            Toast.makeText(this, "موجودی کارت", Toast.LENGTH_SHORT).show()
        }

        iconMyReceipts.setOnClickListener {
            Toast.makeText(this, "قبض‌های من", Toast.LENGTH_SHORT).show()
        }

        // bottom quick actions
        val btnInternet = findViewById<Button>(R.id.action_internet)
        val btnRecharge = findViewById<Button>(R.id.action_recharge)
        val btnCardToCard = findViewById<Button>(R.id.action_card_to_card)

        btnInternet.setOnClickListener {
            Toast.makeText(this, "بسته اینترنت", Toast.LENGTH_SHORT).show()
        }

        btnRecharge.setOnClickListener {
            // example: navigate to a recharge screen
            Toast.makeText(this, "خرید شارژ", Toast.LENGTH_SHORT).show()
        }

        btnCardToCard.setOnClickListener {
            Toast.makeText(this, "کارت به کارت", Toast.LENGTH_SHORT).show()
        }
    }
}

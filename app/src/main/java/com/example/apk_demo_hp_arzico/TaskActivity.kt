package com.example.aplication_aplication_taskman

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Tasks"

        recyclerView = findViewById(R.id.recyclerViewTasks)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchTasks()
    }

    private fun fetchTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = ApiClient.service(this@TaskActivity)
                val response = service.getTasks()

                if (response.isSuccessful) {
                    val taskResponse = response.body()
                    if (taskResponse?.success == true) {
                        val tasks = taskResponse.data ?: emptyList()
                        runOnUiThread {
                            val adapter = TaskAdapter(tasks)
                            recyclerView.adapter = adapter
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@TaskActivity,
                                "Failed: ${taskResponse?.data}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@TaskActivity,
                            "Failed to load tasks: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@TaskActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

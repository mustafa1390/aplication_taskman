package com.example.aplication_aplication_taskman

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SingleTaskActivity : AppCompatActivity() {

    private lateinit var tvTitle: TextView
    private lateinit var tvDescription: TextView
    private lateinit var tvStatus: TextView
    private lateinit var tvCreated: TextView
    private lateinit var progressBar: ProgressBar
    private lateinit var recyclerTimes: RecyclerView
    private lateinit var adapter: TaskTimeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_task)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        tvTitle = findViewById(R.id.tvSingleTitle)
        tvDescription = findViewById(R.id.tvSingleDescription)
        tvStatus = findViewById(R.id.tvSingleStatus)
        tvCreated = findViewById(R.id.tvSingleCreated)
        progressBar = findViewById(R.id.progressBarSingle)
        recyclerTimes = findViewById(R.id.recyclerTaskTimes)
        recyclerTimes.layoutManager = LinearLayoutManager(this)
        adapter = TaskTimeAdapter(mutableListOf())
        recyclerTimes.adapter = adapter

        val taskId = intent.getIntExtra("task_id", -1)
        if (taskId == -1) {
            Toast.makeText(this, "Invalid task id", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        // wire return button
        val btnReturn = findViewById<Button>(R.id.btnReturn)
        btnReturn.setOnClickListener {
            finish()
        }

        fetchSingleTask(taskId)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun fetchSingleTask(id: Int) {
        progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val svc = ApiClient.service(this@SingleTaskActivity)
                val resp = svc.getTaskById(id)
                if (resp.isSuccessful) {
                    val wrapper = resp.body()
                    val data = wrapper?.data
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        if (data != null) {
                            tvTitle.text = data.title
                            tvDescription.text = data.description ?: ""
                            tvStatus.text = data.status ?: ""
                            tvCreated.text = data.createdAt ?: ""
                            val times = data.taskTimes ?: emptyList()
                            adapter.update(times)
                        } else {
                            Toast.makeText(this@SingleTaskActivity, "No data", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    val err = resp.errorBody()?.string() ?: "Error"
                    runOnUiThread {
                        progressBar.visibility = View.GONE
                        Toast.makeText(this@SingleTaskActivity, err, Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    progressBar.visibility = View.GONE
                    Toast.makeText(this@SingleTaskActivity, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}

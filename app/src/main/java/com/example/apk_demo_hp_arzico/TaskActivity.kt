package com.example.aplication_aplication_taskman

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: TaskAdapter
    private val taskList = mutableListOf<TaskItem>()
    private var currentPage = 1
    private var lastPage = 1
    private var isLoading = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Tasks"

        recyclerView = findViewById(R.id.recyclerViewTasks)
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        adapter = TaskAdapter(taskList, recyclerView)
        recyclerView.adapter = adapter

        // Add scroll listener for pagination
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                if (!isLoading && visibleItemCount + firstVisibleItemPosition >= totalItemCount - 5) {
                    if (currentPage < lastPage) {
                        loadNextPage()
                    }
                }
            }
        })

        // wire return button
        val btnReturn = findViewById<FloatingActionButton>(R.id.btnReturn)
        btnReturn.setOnClickListener {
            finish()
        }

        fetchTasks()


    }

    private fun fetchTasks() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = ApiClient.service(this@TaskActivity)
                val response = service.getTasks()

                if (response.isSuccessful) {
                    val paginatedResponse = response.body()
                    paginatedResponse?.let {
                        lastPage = it.meta?.last_page ?: 1
                        val tasks = it.data ?: emptyList()
                        runOnUiThread {
                            taskList.clear()
                            taskList.addAll(tasks)
                            adapter.notifyDataSetChanged()
                            if (taskList.isEmpty()) {
                                Toast.makeText(
                                    this@TaskActivity,
                                    "No tasks found",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
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

    private fun loadNextPage() {
        if (isLoading) return
        isLoading = true
        currentPage++

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = ApiClient.service(this@TaskActivity)
                val response = service.getTasksByPage(currentPage)

                if (response.isSuccessful) {
                    val paginatedResponse = response.body()
                    paginatedResponse?.let {
                        val newTasks = it.data ?: emptyList()
                        runOnUiThread {
                            val previousSize = taskList.size
                            taskList.addAll(newTasks)
                            adapter.notifyItemRangeInserted(previousSize, newTasks.size)
                            isLoading = false
                        }
                    }
                } else {
                    isLoading = false
                }
            } catch (e: Exception) {
                isLoading = false
                runOnUiThread {
                    Toast.makeText(
                        this@TaskActivity,
                        "Error loading more: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

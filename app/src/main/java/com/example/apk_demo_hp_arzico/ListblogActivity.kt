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

class ListblogActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listblog)

        val toolbar = findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)

        recyclerView = findViewById(R.id.recyclerViewBlogs)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchBlogs()
    }

    private fun fetchBlogs() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val service = ApiClient.service(this@ListblogActivity)
                val response = service.getBlogList()

                if (response.isSuccessful) {
                    val remoteList = response.body() ?: emptyList()
                    // map RemoteBlogPost -> UI BlogPost
                    val posts = remoteList.map { remote ->
                        BlogPost(
                            id = remote.id,
                            title = remote.title,
                            content = remote.body,
                            author = "User ${remote.userId}",
                            date = ""
                        )
                    }

                    runOnUiThread {
                        val adapter = BlogAdapter(posts)
                        recyclerView.adapter = adapter
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@ListblogActivity,
                            "Failed to load blogs: ${response.code()}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            } catch (e: Exception) {
                runOnUiThread {
                    Toast.makeText(
                        this@ListblogActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}

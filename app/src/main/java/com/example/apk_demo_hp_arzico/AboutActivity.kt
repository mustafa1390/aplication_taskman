package com.example.aplication_aplication_taskman
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

class AboutActivity : BaseActivity() {


    private lateinit var textTitle: TextView
    private lateinit var textDescription: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)


        textTitle = findViewById(R.id.textTitle)
        textDescription = findViewById(R.id.textDescription)
        progressBar = findViewById(R.id.progressBar)

        fetchAboutData()


        setupNavigation(
            drawerId = R.id.drawer_layout,
            navViewId = R.id.nav_view,
            toolbarId = R.id.toolbar
        )
    }


    private fun fetchAboutData() {
        progressBar.visibility = View.VISIBLE

        // Using Kotlin Coroutines for background thread
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val apiUrl = "https://jsonplaceholder.typicode.com/todos/1"
                val connection = URL(apiUrl).openConnection() as HttpURLConnection
                connection.requestMethod = "GET"

                val response = connection.inputStream.bufferedReader().use { it.readText() }


                val json = JSONObject(response)
                val title = json.getString("title")
                val description = json.getString("id")

                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    textTitle.text = title
                    textDescription.text = description
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    progressBar.visibility = View.GONE
                    textTitle.text = "Error loading data"
                    textDescription.text = e.message
                }
            }
        }
    }

}
package com.example.aplication_aplication_taskman

import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: MutableList<TaskItem>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        private val tvUserId: TextView = itemView.findViewById(R.id.tvTaskUserId)
        private val tvPriority: TextView = itemView.findViewById(R.id.tvTaskPriority)
        private val tvTimer: TextView = itemView.findViewById(R.id.tvTaskTimer)
        private val card: CardView? = itemView.findViewById(R.id.cardTask)
        private val handler = Handler(Looper.getMainLooper())
        private var timerRunnable: Runnable? = null
        private var elapsedSeconds = 0
        private var currentTask: TaskItem? = null

        fun bind(task: TaskItem) {
            currentTask = task
            tvTitle.text = task.title
            tvDescription.text = task.description ?: "No description"
            tvUserId.text = "User ID: ${task.userId}"
            tvPriority.text = "Priority: ${task.priority ?: "N/A"}"

            // Color the card according to status
            val status = task.status?.lowercase()
            when (status) {
                "done" -> card?.setCardBackgroundColor(Color.parseColor("#A5D6A7")) // green
                "notwork" -> card?.setCardBackgroundColor(Color.parseColor("#EF9A9A")) // red
                "inwork" -> card?.setCardBackgroundColor(Color.parseColor("#FFE0B2")) // orange
                else -> card?.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            }

            // set click to open single task
            itemView.setOnClickListener {
                val ctx = itemView.context
                val intent = Intent(ctx, SingleTaskActivity::class.java)
                intent.putExtra("task_id", task.id)
                ctx.startActivity(intent)
            }

            // Show timer for inwork status
            if (status == "inwork") {
                tvTimer.visibility = android.view.View.VISIBLE
                // Initialize elapsedSeconds from priority field
                elapsedSeconds = task.priority ?: 0
                startTimer()
            } else {
                tvTimer.visibility = android.view.View.GONE
                stopTimer()
            }
        }

        private fun startTimer() {
            stopTimer() // Stop any existing timer
            timerRunnable = object : Runnable {
                override fun run() {
                    elapsedSeconds++
                    val hours = elapsedSeconds / 3600
                    val minutes = (elapsedSeconds % 3600) / 60
                    val seconds = elapsedSeconds % 60
                    tvTimer.text = String.format("%02d:%02d:%02d", hours, minutes, seconds)
                    handler.postDelayed(this, 1000)
                }
            }
            handler.post(timerRunnable!!)
        }

        private fun stopTimer() {
            timerRunnable?.let { handler.removeCallbacks(it) }
            timerRunnable = null
        }

        fun onViewRecycled() {
            stopTimer()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)
        return TaskViewHolder(view)
    }

    override fun onBindViewHolder(holder: TaskViewHolder, position: Int) {
        holder.bind(tasks[position])
    }

    override fun onViewRecycled(holder: TaskViewHolder) {
        super.onViewRecycled(holder)
        holder.onViewRecycled()
    }

    override fun getItemCount(): Int = tasks.size
}

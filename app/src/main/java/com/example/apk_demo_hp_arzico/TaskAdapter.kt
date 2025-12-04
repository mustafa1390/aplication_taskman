package com.example.aplication_aplication_taskman

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: MutableList<TaskItem>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        private val tvUserId: TextView = itemView.findViewById(R.id.tvTaskUserId)
        private val tvPriority: TextView = itemView.findViewById(R.id.tvTaskPriority)
        private val card: CardView? = itemView.findViewById(R.id.cardTask)

        fun bind(task: TaskItem) {
            tvTitle.text = task.title
            tvDescription.text = task.description ?: "No description"
            tvUserId.text = "User ID: ${task.userId}"
            tvPriority.text = "Priority: ${task.priority ?: "N/A"}"

            // Color the card according to status
            val status = task.status?.lowercase()
            when (status) {
                "done" -> card?.setCardBackgroundColor(Color.parseColor("#A5D6A7")) // green-ish
                "notwork" -> card?.setCardBackgroundColor(Color.parseColor("#EF9A9A")) // red-ish
                else -> card?.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            }
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

    override fun getItemCount(): Int = tasks.size
}

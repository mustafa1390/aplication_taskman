package com.example.aplication_aplication_taskman

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TaskAdapter(private val tasks: List<TaskItem>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        private val tvStatus: TextView = itemView.findViewById(R.id.tvTaskStatus)
        private val tvPriority: TextView = itemView.findViewById(R.id.tvTaskPriority)

        fun bind(task: TaskItem) {
            tvTitle.text = task.title
            tvDescription.text = task.description ?: "No description"
            tvStatus.text = "Status: ${task.status ?: "N/A"}"
            tvPriority.text = "Priority: ${task.priority ?: "N/A"}"
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

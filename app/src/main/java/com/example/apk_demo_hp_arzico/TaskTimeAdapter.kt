package com.example.aplication_aplication_taskman

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskTimeAdapter(private val items: MutableList<TaskTimeItem>) :
    RecyclerView.Adapter<TaskTimeAdapter.TimeViewHolder>() {

    inner class TimeViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvId: TextView = itemView.findViewById(R.id.tvTimeId)
        private val tvDesc: TextView = itemView.findViewById(R.id.tvTimeDesc)
        private val tvCreated: TextView = itemView.findViewById(R.id.tvTimeCreated)

        fun bind(it: TaskTimeItem) {
            tvId.text = "#${it.id}"
            tvDesc.text = it.description?.toString() ?: ""
            tvCreated.text = it.createdAt ?: ""

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_task_time, parent, false)
        return TimeViewHolder(v)
    }

    override fun onBindViewHolder(holder: TimeViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    fun update(newItems: List<TaskTimeItem>) {
        items.clear()
        items.addAll(newItems)
        notifyDataSetChanged()
    }
}

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
        private val btnStart: Button = itemView.findViewById(R.id.btnStartTaskTime)
        private val btnStop: Button = itemView.findViewById(R.id.btnStopTaskTime)

        fun bind(it: TaskTimeItem) {
            tvId.text = "#${it.id}"
            tvDesc.text = it.description?.toString() ?: ""
            tvCreated.text = it.createdAt ?: ""

            // Handle start button click
            btnStart.setOnClickListener {
                showPercentDialog(it.id)
            }

            // Handle stop button click
            btnStop.setOnClickListener {
                Toast.makeText(itemView.context, "Stop clicked", Toast.LENGTH_SHORT).show()
            }
        }

        private fun showPercentDialog(taskTimeId: Int) {
            val context = itemView.context
            val dialogView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_task_time_update, null)
            
            val etPercent = dialogView.findViewById<TextInputEditText>(R.id.etPercent)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
            val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmit)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            btnCancel.setOnClickListener {
                dialog.dismiss()
            }

            btnSubmit.setOnClickListener {
                val percentStr = etPercent.text.toString().trim()
                if (percentStr.isEmpty()) {
                    Toast.makeText(context, "Please enter percent", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                val percent = percentStr.toIntOrNull()
                if (percent == null || percent < 0 || percent > 100) {
                    Toast.makeText(context, "Percent must be 0-100", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }

                // Disable button and submit
                btnStart.isEnabled = false
                dialog.dismiss()

                updateTaskTime(taskTimeId, percent)
            }

            dialog.show()
        }

        private fun updateTaskTime(taskTimeId: Int, percent: Int) {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val svc = ApiClient.service(itemView.context)
                    val req = UpdateTaskTimeRequest(task_time_id = taskTimeId, percent = percent)
                    val resp = svc.updateTaskTime(req)

                    if (resp.isSuccessful) {
                        val body = resp.body()
                        if (body?.success == true) {
                            CoroutineScope(Dispatchers.Main).launch {
                                Toast.makeText(
                                    itemView.context,
                                    body.message ?: "Updated successfully",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        } else {
                            CoroutineScope(Dispatchers.Main).launch {
                                btnStart.isEnabled = true
                                Toast.makeText(
                                    itemView.context,
                                    body?.message ?: "Update failed",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    } else {
                        CoroutineScope(Dispatchers.Main).launch {
                            btnStart.isEnabled = true
                            Toast.makeText(
                                itemView.context,
                                "Error: ${resp.code()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } catch (e: Exception) {
                    CoroutineScope(Dispatchers.Main).launch {
                        btnStart.isEnabled = true
                        Toast.makeText(itemView.context, "Exception: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
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

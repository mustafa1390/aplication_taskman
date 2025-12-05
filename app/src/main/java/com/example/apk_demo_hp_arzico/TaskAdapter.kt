package com.example.aplication_aplication_taskman

import android.app.AlertDialog
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TaskAdapter(private val tasks: MutableList<TaskItem>) :
    RecyclerView.Adapter<TaskAdapter.TaskViewHolder>() {

    inner class TaskViewHolder(itemView: android.view.View) : RecyclerView.ViewHolder(itemView) {
        private val tvTitle: TextView = itemView.findViewById(R.id.tvTaskTitle)
        private val tvProject: TextView = itemView.findViewById(R.id.tvTaskProject)
        private val tvPhase: TextView = itemView.findViewById(R.id.tvTaskPhase)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvTaskDescription)
        private val tvUserId: TextView = itemView.findViewById(R.id.tvTaskUserId)
        private val tvPriority: TextView = itemView.findViewById(R.id.tvTaskPriority)
        private val tvDuringLive: TextView = itemView.findViewById(R.id.tvDuringLive)
        private val tvTimer: TextView = itemView.findViewById(R.id.tvTaskTimer)
        private val card: CardView? = itemView.findViewById(R.id.cardTask)
        private val handler = Handler(Looper.getMainLooper())
        private var timerRunnable: Runnable? = null
        private var elapsedSeconds = 0
        private var currentTask: TaskItem? = null
        private val btnStart: Button = itemView.findViewById(R.id.btnStartTaskTime)
        private val btnStop: Button = itemView.findViewById(R.id.btnStopTaskTime)

        fun bind(task: TaskItem) {
            currentTask = task
            tvTitle.text = task.title
            tvProject.text =  " پروژه: ${task.project}"
            tvPhase.text =  " فاز: ${task.phase}"
            tvDuringLive.text =  " ساعت کارکرد :  ${task.dtimefrmt}"
            tvDescription.text = task.description ?: "No description"
            tvUserId.text = "User ID: ${task.userId}"
            tvPriority.text = "Priority: ${task.priority ?: "N/A"}"

            // Color the card according to status
            val flag = task.flag?.lowercase()
            val status = task.status?.lowercase()
            when (flag) {
                "done" -> card?.setCardBackgroundColor(Color.parseColor("#A5D6A7")) // green
                "notwork" -> card?.setCardBackgroundColor(Color.parseColor("#EF9A9A")) // red
                "running" -> card?.setCardBackgroundColor(Color.parseColor("#00acd2")) // orange
                else -> card?.setCardBackgroundColor(Color.parseColor("#FFFFFF"))
            }

            // set click to open single task
            itemView.setOnClickListener {
                val ctx = itemView.context
                val intent = Intent(ctx, SingleTaskActivity::class.java)
                intent.putExtra("task_id", task.id)
                ctx.startActivity(intent)
            }

            // Show timer for running flag
            if (flag == "running") {
                tvTimer.visibility = android.view.View.VISIBLE
                // Initialize elapsedSeconds from priority field
                elapsedSeconds = task.duringLive ?: 0
                startTimer()
            } else {
                tvTimer.visibility = android.view.View.GONE
                stopTimer()
            }




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
                .inflate(R.layout.dialog_task_update, null)

            val tvTaskId = dialogView.findViewById<TextView>(R.id.tvTaskId)
            val etPercent = dialogView.findViewById<TextInputEditText>(R.id.etPercent)
            val btnCancel = dialogView.findViewById<Button>(R.id.btnCancel)
            val btnSubmit = dialogView.findViewById<Button>(R.id.btnSubmit)

            val dialog = AlertDialog.Builder(context)
                .setView(dialogView)
                .create()

            // populate task id into dialog
            tvTaskId.text = currentTask?.id?.toString() ?: "-"

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
                    val req = UpdateTaskTimeRequest(
                        task_id = currentTask?.id ?: 0,
                        task_time_id = taskTimeId,
                        percent = percent
                    )
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

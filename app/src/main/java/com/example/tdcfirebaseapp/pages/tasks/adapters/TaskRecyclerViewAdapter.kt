package com.example.tdcfirebaseapp.pages.tasks.adapters

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import com.example.tdcfirebaseapp.databinding.FragmentTaskItemBinding
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.google.android.material.checkbox.MaterialCheckBox
import java.text.SimpleDateFormat
import java.util.*

class TaskRecyclerViewAdapter(
    private val values: List<Task>
) : RecyclerView.Adapter<TaskRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            FragmentTaskItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values[position]

        holder.title.text = item.title
        holder.checkbox.isChecked = item.done

        if (item.date != null) {
            val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale.ROOT)
            val formattedDate = formatter.format(item.date!!)
            holder.date.text = formattedDate
        } else {
            holder.date.visibility = View.GONE
        }
    }

    override fun getItemCount(): Int = values.size

    inner class ViewHolder(binding: FragmentTaskItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title: TextView = binding.taskTitle
        val checkbox: MaterialCheckBox = binding.itemCheckbox
        val date: TextView = binding.taskDate
    }

}
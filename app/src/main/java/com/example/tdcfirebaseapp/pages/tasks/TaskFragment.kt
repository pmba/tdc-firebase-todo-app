package com.example.tdcfirebaseapp.pages.tasks

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.databinding.FragmentTaskBinding
import com.example.tdcfirebaseapp.pages.tasks.adapters.TodoTaskRecyclerViewAdapter
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.google.android.material.appbar.MaterialToolbar

class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater, container, false)

        initRecyclerView()

        return binding.root
    }

    private fun initRecyclerView() {
        val tasks = IntArray(20).map { index ->
            Task(
                title = "Atividade $index",
                done = false
            )
        }

        val recyclerView = binding.taskListRecyclerView
        with(recyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = TodoTaskRecyclerViewAdapter(tasks)
        }
    }
}
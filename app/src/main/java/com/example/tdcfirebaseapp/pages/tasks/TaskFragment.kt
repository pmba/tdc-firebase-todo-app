package com.example.tdcfirebaseapp.pages.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdcfirebaseapp.databinding.FragmentTaskBinding
import com.example.tdcfirebaseapp.pages.tasks.adapters.TaskRecyclerViewAdapter
import com.example.tdcfirebaseapp.pages.tasks.viewmodels.TaskViewModel

class TaskFragment : Fragment() {

    private lateinit var binding: FragmentTaskBinding

    private lateinit var mAdapter: TaskRecyclerViewAdapter
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mViewModel: TaskViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater, container, false)

        mRecyclerView = binding.taskListRecyclerView

        mViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)
        mViewModel.init()

        initRecyclerView()
        setupViewModel()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupViewModel() {
        mViewModel.getTasks().observe(requireActivity()) { tasks ->
            mRecyclerView.setItemViewCacheSize(tasks.size)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        mAdapter = TaskRecyclerViewAdapter(mViewModel.getTasks().value!!)

        with(mRecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }
}
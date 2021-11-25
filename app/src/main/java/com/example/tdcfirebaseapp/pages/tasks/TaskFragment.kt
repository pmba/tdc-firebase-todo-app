package com.example.tdcfirebaseapp.pages.tasks

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.tdcfirebaseapp.databinding.FragmentTaskBinding
import com.example.tdcfirebaseapp.pages.tasks.adapters.TaskRecyclerViewAdapter
import com.example.tdcfirebaseapp.pages.tasks.dialogfragments.EditTaskModalBottomSheet
import com.example.tdcfirebaseapp.pages.tasks.dialogfragments.NewTaskModalBottomSheet
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.example.tdcfirebaseapp.pages.tasks.repositories.TaskRepository
import com.example.tdcfirebaseapp.pages.tasks.repositories.TaskRepository.Instance.TaskType
import com.example.tdcfirebaseapp.pages.tasks.viewmodels.TaskViewModel
import com.example.tdcfirebaseapp.shared.contracts.TaskAdapterContract
import kotlin.properties.Delegates

class TaskFragment : Fragment(), TaskAdapterContract {

    private lateinit var binding: FragmentTaskBinding

    private lateinit var mAdapter: TaskRecyclerViewAdapter
    private lateinit var mRecyclerView: RecyclerView

    private lateinit var mViewModel: TaskViewModel

    private var showAllTasks by Delegates.notNull<Boolean>()
    private var showOnlyTasks: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let { args ->
            showAllTasks = args.getBoolean("showAllTasks")
            showOnlyTasks = args.getString("showOnlyTasks")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentTaskBinding.inflate(inflater, container, false)

        mRecyclerView = binding.taskListRecyclerView

        mViewModel = ViewModelProvider(this).get(TaskViewModel::class.java)

        val typesToLoad = when {
            showAllTasks -> TaskType.ALL
            showOnlyTasks == "finished" -> TaskType.FINISHED
            else -> TaskType.UNFINISHED
        }

        mViewModel.init(typesToLoad)

        initRecyclerView()
        setupButtons()
        setupViewModel()

        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupViewModel() {
        mViewModel.getTasks().observe(requireActivity()) { tasks ->
            Log.d(TAG, "getTasks().observe : $tasks")
            mRecyclerView.setItemViewCacheSize(tasks.size)
            mAdapter.notifyDataSetChanged()
        }
    }

    private fun initRecyclerView() {
        mAdapter = TaskRecyclerViewAdapter(
            mViewModel.getTasks().value!!,
            this,
            requireActivity()
        )

        with(mRecyclerView) {
            layoutManager = LinearLayoutManager(context).apply {
                reverseLayout = true
                stackFromEnd = true
            }

            adapter = mAdapter
        }
    }

    private fun setupButtons() {
        binding.newTaskButton.setOnClickListener {
            NewTaskModalBottomSheet(mViewModel)
                .show(parentFragmentManager, NewTaskModalBottomSheet.TAG)
        }
    }

    override fun onEditRequest(task: Task) {
        EditTaskModalBottomSheet(mViewModel, task)
            .show(parentFragmentManager, EditTaskModalBottomSheet.TAG)
    }

    companion object {
        const val TAG = "TaskFragment"
    }
}
package com.example.tdcfirebaseapp.pages.tasks.modals

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.databinding.FragmentEditTaskBinding
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.example.tdcfirebaseapp.pages.tasks.viewmodels.TaskViewModel
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class EditTaskModalBottomSheet(
    private val viewModel: TaskViewModel,
    private val currentTask: Task
): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditTaskBinding

    private val taskBuilder: Task.Builder = Task.Builder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        setupAppBar()
        setupTaskInfo()
        setupDatePicker()
        setupButtons()

        return binding.root
    }

    private fun setupTaskInfo() {
        binding.editTaskTitleTextField.setText(currentTask.title)

        currentTask.date?.let { taskDate ->
            val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale.ROOT)
            val formattedDate = formatter.format(taskDate.time)
            binding.editTaskEndpointTextField.setText(formattedDate)
        }
    }

    private fun setupAppBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.close_fragment -> {
                    dismiss()
                    true
                }
                else -> false
            }
        }
    }

    private fun setupButtons() {
        binding.deleteTaskButton.setOnClickListener {
            dismiss()
            viewModel.removeTask(currentTask.uid)
        }

        binding.editTaskButton.setOnClickListener {
            val taskTitle = binding.editTaskTitleTextField.text.toString()

            if (taskTitle.isBlank()) {
                binding.editTaskEndpointTextInput.error = "Campo não pode ser vazio"
                return@setOnClickListener
            }

            taskBuilder.setUid(currentTask.uid)
            taskBuilder.setTitle(taskTitle)

            viewModel.updateTask(currentTask.uid, taskBuilder.build())

            dismiss()
        }
    }

    private fun setupDatePicker() {
        binding.editTaskEndpointTextField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val calendarConstraint = CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()

                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(calendarConstraint)
                    .build()

                val endpointTextField = binding.editTaskEndpointTextField

                datePicker.addOnPositiveButtonClickListener { timeInMillis ->
                    val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale.ROOT)
                    val calendar = Calendar.getInstance()
                    calendar.timeInMillis = timeInMillis
                    calendar.add(Calendar.DAY_OF_YEAR, 1)
                    val formattedDate = formatter.format(calendar.timeInMillis)

                    endpointTextField.setText(formattedDate)
                    endpointTextField.clearFocus()

                    taskBuilder.setDate(calendar.time)
                }

                datePicker.addOnCancelListener { endpointTextField.clearFocus() }
                datePicker.addOnDismissListener { endpointTextField.clearFocus() }

                datePicker.show(parentFragmentManager, NewTaskModalBottomSheet.TAG)
            }
        }
    }

    companion object {
        const val TAG = "EditTaskModalBottomSheet"
    }
}
package com.example.tdcfirebaseapp.pages.tasks.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditTaskBinding.inflate(inflater, container, false)

        setupAppBar()
        setupTaskInfo()
        setupTextField()
        setupDatePicker()
        setupButtons()

        return binding.root
    }

    private fun setupTaskInfo() {
        binding.editTaskTitleTextField.setText(currentTask.title)

        val taskDate = currentTask.date

        if (taskDate != null) {
            val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale.ROOT)
            val formattedDate = formatter.format(taskDate.time)
            binding.editTaskDateChip.text = formattedDate

            taskBuilder.setDate(taskDate)
            showDateChip(true)
        } else {
            showDateChip(false)
        }
    }

    private fun setupTextField() {
        binding.editTaskTitleTextField.addTextChangedListener { text ->
            requireActivity().runOnUiThread {
                binding.editTaskButton.isEnabled = text?.isNotBlank() ?: false
            }
        }
    }

    private fun setupAppBar() {
        binding.topAppBar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.remove_task -> {
                    dismiss()
                    requireRemoveTask()
                    true
                }
                else -> false
            }
        }

        binding.topAppBar.setNavigationOnClickListener {
            dismiss()
        }
    }


    private fun setupButtons() {

        binding.editTaskButton.setOnClickListener {
            val taskTitle = binding.editTaskTitleTextField.text.toString()

            taskBuilder.setUid(currentTask.uid)
            taskBuilder.setDone(currentTask.done)
            taskBuilder.setTitle(taskTitle)

            viewModel.updateTask(currentTask.uid, taskBuilder.build())

            dismiss()
        }
    }

    private fun requireRemoveTask() {
        viewModel.removeTask(currentTask.uid)
    }

    private fun setupDatePicker() {
        binding.editTaskEndpointCardView.setOnClickListener {
            val calendarConstraint = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(calendarConstraint)
                .build()

            val endpointChip = binding.editTaskDateChip

            datePicker.addOnPositiveButtonClickListener { timeInMillis ->
                val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale.ROOT)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timeInMillis
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val formattedDate = formatter.format(calendar.timeInMillis)

                endpointChip.text = formattedDate
                showDateChip(true)

                binding.editTaskButton.isEnabled = true

                taskBuilder.setDate(calendar.time)
            }

            datePicker.addOnCancelListener { endpointChip.clearFocus() }
            datePicker.addOnDismissListener { endpointChip.clearFocus() }

            datePicker.show(parentFragmentManager, NewTaskModalBottomSheet.TAG)
        }

        binding.editTaskDateChip.setOnCloseIconClickListener {
            taskBuilder.setDate(null)
            showDateChip(false)
        }
    }

    private fun showDateChip(show: Boolean) {
        if (show) {
            binding.editTaskAddDateTextView.visibility = View.GONE
            binding.editTaskDateChip.visibility = View.VISIBLE
        } else {
            binding.editTaskAddDateTextView.visibility = View.VISIBLE
            binding.editTaskDateChip.visibility = View.GONE
        }
    }

    companion object {
        const val TAG = "EditTaskModalBottomSheet"
    }
}
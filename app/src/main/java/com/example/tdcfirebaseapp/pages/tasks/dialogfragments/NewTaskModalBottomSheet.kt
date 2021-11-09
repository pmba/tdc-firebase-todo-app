package com.example.tdcfirebaseapp.pages.tasks.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.databinding.FragmentNewTaskBinding
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.example.tdcfirebaseapp.pages.tasks.viewmodels.TaskViewModel
import com.example.tdcfirebaseapp.shared.utils.showSoftKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.util.*

class NewTaskModalBottomSheet(
    private val viewModel: TaskViewModel
): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskBinding

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
        binding = FragmentNewTaskBinding.inflate(inflater, container, false)

        setupDatePicker()
        setupCreateTaskButton()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requestInputFocus()
    }

    private fun requestInputFocus() {
        requireActivity().let { activity ->
            activity.runOnUiThread {
                activity.showSoftKeyboard(binding.newTaskTitleTextField)
            }
        }
    }

    private fun setupCreateTaskButton() {
        binding.createNewTaskButton.setOnClickListener {
            val taskTitle = binding.newTaskTitleTextField.text.toString()

            if (taskTitle.isBlank()) {
                binding.newTaskTitleTextInput.error = "Campo não pode ser vazio"
                return@setOnClickListener
            }

            taskBuilder.setTitle(binding.newTaskTitleTextField.text.toString())
            taskBuilder.setUid(UUID.randomUUID().toString())
            viewModel.addNewTask(taskBuilder.build())
            dismiss()
        }
    }

    private fun setupDatePicker() {
        binding.createNewTaskEndpointButton.setOnClickListener {
            val calendarConstraint = CalendarConstraints.Builder()
                .setValidator(DateValidatorPointForward.now())
                .build()

            val datePicker = MaterialDatePicker.Builder.datePicker()
                .setCalendarConstraints(calendarConstraint)
                .build()

            val endpointChip = binding.createNewTaskDateChip

            datePicker.addOnPositiveButtonClickListener { timeInMillis ->
                val formatter = SimpleDateFormat("EEEE, dd 'de' MMMM", Locale.ROOT)
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = timeInMillis
                calendar.add(Calendar.DAY_OF_YEAR, 1)
                val formattedDate = formatter.format(calendar.timeInMillis)

                endpointChip.text = formattedDate
                endpointChip.visibility = View.VISIBLE

                taskBuilder.setDate(calendar.time)
            }

            datePicker.show(parentFragmentManager, TAG)
        }

        binding.createNewTaskDateChip.setOnCloseIconClickListener { chip ->
            chip.visibility = View.GONE

            taskBuilder.setDate(null)
        }
    }

    companion object {
        const val TAG = "NewTaskModalBottomSheet"
    }
}
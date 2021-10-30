package com.example.tdcfirebaseapp.pages.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.tdcfirebaseapp.databinding.FragmentNewTaskBinding
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.example.tdcfirebaseapp.shared.contracts.CreateTaskContract
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.datepicker.CalendarConstraints
import com.google.android.material.datepicker.DateValidatorPointForward
import com.google.android.material.datepicker.MaterialDatePicker
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class NewTaskModalBottomSheet(
    private val listener: CreateTaskContract
): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentNewTaskBinding

    private val taskBuilder: Task.Builder = Task.Builder()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNewTaskBinding.inflate(inflater, container, false)

        setupDatePicker()
        binding.createNewTaskButton.setOnClickListener {
            val taskTitle = binding.newTaskTitleTextField.text.toString()

            if (taskTitle.isBlank()) {
                binding.newTaskTitleTextInput.error = "Campo não pode ser vazio"
                return@setOnClickListener
            }

            taskBuilder.setTitle(binding.newTaskTitleTextField.text.toString())
            listener.onCreateNewTask(taskBuilder.build())
            dismiss()
        }

        return binding.root
    }

    private fun setupDatePicker() {
        binding.newTaskEndpointTextField.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus) {
                val calendarConstraint = CalendarConstraints.Builder()
                    .setValidator(DateValidatorPointForward.now())
                    .build()

                val datePicker = MaterialDatePicker.Builder.datePicker()
                    .setCalendarConstraints(calendarConstraint)
                    .build()

                val endpointTextField = binding.newTaskEndpointTextField

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

                datePicker.show(parentFragmentManager, TAG)
            }
        }
    }

    companion object {
        const val TAG = "NewTaskModalBottomSheet"
    }
}
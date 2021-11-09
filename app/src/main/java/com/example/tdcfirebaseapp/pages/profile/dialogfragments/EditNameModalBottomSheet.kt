package com.example.tdcfirebaseapp.pages.profile.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.databinding.FragmentEditNameBinding
import com.example.tdcfirebaseapp.pages.profile.viewmodels.ProfileViewModel
import com.example.tdcfirebaseapp.shared.utils.showSoftKeyboard
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class EditNameModalBottomSheet(
    private val viewModel: ProfileViewModel
): BottomSheetDialogFragment() {

    private lateinit var binding: FragmentEditNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEditNameBinding.inflate(inflater, container, false)

        binding.editNameTextField.setText(viewModel.getName().value)

        setupSaveButton()

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        requestInputFocus()
    }

    private fun requestInputFocus() {
        requireActivity().let { activity ->
            activity.runOnUiThread {
                activity.showSoftKeyboard(binding.editNameTextField)
            }
        }
    }

    private fun setupSaveButton() {
        binding.editNameButton.setOnClickListener {
            dismiss()
        }
    }

    companion object {
        const val TAG = "EditNameModalBottomSheet"
    }
}
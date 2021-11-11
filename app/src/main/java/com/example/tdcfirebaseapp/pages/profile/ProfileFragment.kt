package com.example.tdcfirebaseapp.pages.profile

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tdcfirebaseapp.databinding.FragmentProfileBinding
import com.example.tdcfirebaseapp.pages.profile.dialogfragments.EditNameModalBottomSheet
import com.example.tdcfirebaseapp.pages.profile.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var mViewModel: ProfileViewModel

    private lateinit var requestLauncher: ActivityResultLauncher<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
                Log.d(TAG, "PERMISSION_IS_GRANTED")
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
                Log.d(TAG, "PERMISSION_DENIED")
            }
        }
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setupLogoutButton()
        setupNameCardView()
        setupProfileImageButton()

        setupViewModel()

        return binding.root
    }

    private fun setupProfileImageButton() {
        val changeImageButton = binding.changeProfileImageButton

        changeImageButton.setOnClickListener {
            when {
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    Log.d(TAG, "PERMISSION_GRANTED")
                }

                else -> {
                    Log.d(TAG, "REQUEST_LAUNCH")
                    requestLauncher.launch(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                }
            }
        }
    }

    private fun setupNameCardView() {
        binding.profileNameCardView.setOnClickListener {
            requireActivity().runOnUiThread {
                EditNameModalBottomSheet(mViewModel)
                    .show(parentFragmentManager, EditNameModalBottomSheet.TAG)
            }
        }
    }

    private fun setupLogoutButton() {
        binding.logoutButton.setOnClickListener {
            mViewModel.logout()
        }
    }

    private fun setupViewModel() {
        mViewModel.getName().observe(requireActivity()) { userName ->
            binding.profileNameTextView.text = userName
        }

        mViewModel.getEmail().observe(requireActivity()) { userEmail ->
            binding.profileEmailTextView.text = userEmail
        }

        mViewModel.init()
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}
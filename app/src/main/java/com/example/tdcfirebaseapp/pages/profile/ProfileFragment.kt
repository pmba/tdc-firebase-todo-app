package com.example.tdcfirebaseapp.pages.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tdcfirebaseapp.MainActivity
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.databinding.FragmentProfileBinding
import com.example.tdcfirebaseapp.pages.profile.dialogfragments.EditNameModalBottomSheet
import com.example.tdcfirebaseapp.pages.profile.viewmodels.ProfileViewModel
import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts
import com.google.android.material.snackbar.Snackbar
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import java.lang.Exception

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var mViewModel: ProfileViewModel

    private lateinit var requestLauncher: ActivityResultLauncher<String>
    private lateinit var cameraIntentLauncher: ActivityResultLauncher<Intent>

    private lateinit var profileImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestLauncher = registerForActivityResult(RequestPermission()) { isGranted ->
            if (isGranted) {
                launchCameraIntent()
            } else {
                Toast.makeText(requireContext(), "Permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

        cameraIntentLauncher = registerForActivityResult(StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                result.data?.let { data ->
                    val selectedImageUri = data.data

                    if (selectedImageUri != null) {
                        try {

                            val inputStream = requireActivity().contentResolver.openInputStream(selectedImageUri)
                            val bitmap = BitmapFactory.decodeStream(inputStream)
                            profileImageView.setImageBitmap(bitmap)

                            mViewModel.uploadProfileImage(bitmap)
                        } catch (exception: Exception) {
                            Toast.makeText(requireContext(), exception.message, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
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

        profileImageView = binding.profileImageView

        return binding.root
    }

    private fun setupProfileImageButton() {
        val changeImageButton = binding.changeProfileImageButton

        changeImageButton.setOnClickListener {

            when(
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                PackageManager.PERMISSION_GRANTED -> {
                    // You can use the API that requires the permission.
                    Log.d(TAG, "PERMISSION_GRANTED")
                    launchCameraIntent()
                }

                else -> {
                    Log.d(TAG, "REQUEST_LAUNCH")
                    requestLauncher.launch(
                        Manifest.permission.READ_EXTERNAL_STORAGE
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
        requireActivity().runOnUiThread {
            binding.logoutButton.setOnClickListener {
                mViewModel.logout(object : ViewModelContracts.ResultListener {
                    override fun onSuccess() {
                        val mainActivity = requireActivity() as MainActivity
                        mainActivity.requestAppReload()
                    }

                    override fun onFailure(exception: Exception) {
                        Snackbar.make(
                            binding.root,
                            "Logout Failed: ${exception.message}",
                            Snackbar.LENGTH_SHORT
                        ).show()
                    }
                })
            }
        }
    }

    private fun setupViewModel() {
        mViewModel.getName().observe(requireActivity()) { userName ->
            requireActivity().runOnUiThread {
                binding.profileNameTextView.text = userName
            }
        }

        mViewModel.getEmail().observe(requireActivity()) { userEmail ->
            requireActivity().runOnUiThread {
                binding.profileEmailTextView.text = userEmail
            }
        }

        mViewModel.getProfileImage().observe(requireActivity()) { imageUri ->
            requireActivity().runOnUiThread {
                binding.progressIndicator.visibility = View.VISIBLE
                Picasso.get()
                    .load(imageUri)
                    .placeholder(R.drawable.avatar_placeholder)
                    .error(R.drawable.avatar_placeholder)
                    .into(binding.profileImageView, object : Callback {
                        override fun onSuccess() {
                            binding.progressIndicator.visibility = View.GONE
                        }

                        override fun onError(e: Exception?) {
                            binding.progressIndicator.visibility = View.GONE
                        }
                    })
            }
        }

        mViewModel.getIsUploadingImage().observe(requireActivity()) { isLoadingImage ->
            requireActivity().runOnUiThread {
                binding.changeProfileImageButton.isEnabled = !isLoadingImage
                binding.progressIndicator.visibility = if (isLoadingImage) View.VISIBLE else View.GONE
            }
        }

        mViewModel.init(requireActivity()) { initializationError ->
            if (initializationError != null) {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun launchCameraIntent() {
        val cameraIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        cameraIntentLauncher.launch(cameraIntent)
    }

    companion object {
        const val TAG = "ProfileFragment"
    }
}
package com.example.tdcfirebaseapp.pages.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.tdcfirebaseapp.databinding.FragmentProfileBinding
import com.example.tdcfirebaseapp.pages.profile.dialogfragments.EditNameModalBottomSheet
import com.example.tdcfirebaseapp.pages.profile.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    private lateinit var mViewModel: ProfileViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        mViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        setupLogoutButton()
        setupNameCardView()

        setupViewModel()

        return binding.root
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
}
package com.example.tdcfirebaseapp.pages.auth.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.databinding.ActivityForgotPasswordBinding
import com.example.tdcfirebaseapp.pages.auth.forgotpassword.viewmodels.ForgotPasswordViewModel
import com.example.tdcfirebaseapp.shared.utils.hideKeyboard
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    private lateinit var mViewModel: ForgotPasswordViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupButtonAction()
        setupNavBar()
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider(this).get(ForgotPasswordViewModel::class.java)

        mViewModel.isLoading().observe(this) { isLoading ->
            binding.forgotPasswordButton.isEnabled = !isLoading

            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mViewModel.hasErrors().observe(this) { error ->
            if (error != null) {
                Snackbar.make(
                    binding.root,
                    "Something went wrong: ${error.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        mViewModel.hasSentEmail().observe(this) { hasSentEmail ->
            if (hasSentEmail) {
                MaterialAlertDialogBuilder(this)
                    .setTitle(getString(R.string.title_forgot_password_dialog))
                    .setMessage(getString(R.string.message_forgot_password_dialog))
                    .setNeutralButton("Ok") { dialog, _ ->
                        dialog.dismiss()
                        setResult(RESULT_OK)
                        finish()
                    }.show()
            }
        }
    }

    private fun setupButtonAction() {
        binding.forgotPasswordButton.setOnClickListener {
            hideKeyboard()

            val email = binding.emailTextField.editText!!.text.toString()

            mViewModel.sendPasswordResetEmail(email)
        }
    }

    private fun setupNavBar() {
        binding.topAppBar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
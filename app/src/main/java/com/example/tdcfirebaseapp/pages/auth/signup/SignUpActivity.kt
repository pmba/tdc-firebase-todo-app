package com.example.tdcfirebaseapp.pages.auth.signup

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tdcfirebaseapp.databinding.ActivitySignUpBinding
import com.example.tdcfirebaseapp.shared.utils.hideKeyboard
import com.example.tdcfirebaseapp.pages.auth.signup.viewmodels.SignUpViewModel
import com.google.android.material.snackbar.Snackbar

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding

    private lateinit var mViewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupNavBar()
        setupCreateAccountButton()
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider(this).get(SignUpViewModel::class.java)

        mViewModel.isLoading().observe(this) { isLoading ->
            binding.createAccountButton.isEnabled = !isLoading

            binding.progressIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        mViewModel.hasErrors().observe(this) { error ->
            if (error != null) {
                Snackbar.make(
                    binding.root,
                    "Authentication Failed: ${error.message}",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }

        mViewModel.hasSignedUp().observe(this) { hasSignedUp ->
            if (hasSignedUp) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun setupCreateAccountButton() {
        binding.createAccountButton.setOnClickListener {
            hideKeyboard()

            val email = binding.emailTextField.editText!!.text.toString()
            val password = binding.passwordTextField.editText!!.text.toString()
            val confirmation = binding.passwordConfirmationTextField.editText!!.text.toString()

            if (mViewModel.validateFields(email, password, confirmation)) {
                mViewModel.signUpWithEmailAndPassword(email, password)
            }
        }
    }

    private fun setupNavBar() {
        binding.topAppBar.setNavigationOnClickListener {
            setResult(RESULT_CANCELED)
            finish()
        }
    }
}
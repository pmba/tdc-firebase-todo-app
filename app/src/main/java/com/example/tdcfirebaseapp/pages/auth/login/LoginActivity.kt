package com.example.tdcfirebaseapp.pages.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.tdcfirebaseapp.databinding.ActivityLoginBinding
import com.example.tdcfirebaseapp.pages.auth.forgotpassword.ForgotPasswordActivity
import com.example.tdcfirebaseapp.pages.auth.login.viewmodels.LoginViewModel
import com.example.tdcfirebaseapp.shared.utils.hideKeyboard
import com.example.tdcfirebaseapp.pages.auth.signup.SignUpActivity
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var mViewModel: LoginViewModel

    private val signUpResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("LoginActivity", "signUpResultLauncher : ${result.resultCode}")
        finishIfResultOk(result)
    }

    private val forgotPasswordLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        Log.d("LoginActivity", "forgotPasswordLauncher : ${result.resultCode}")
        finishIfResultOk(result)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupViewModel()
        setupSignInButton()
        setupSignUpButton()
        setupForgotPasswordButtons()
    }

    private fun setupViewModel() {
        mViewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        mViewModel.isLoading().observe(this) { isLoading ->
            binding.signInButton.isEnabled = !isLoading
            binding.createAccountButton.isEnabled = !isLoading
            binding.forgotPasswordButton.isEnabled = !isLoading

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

        mViewModel.hasLoggedIn().observe(this) { hasLoggedIn ->
            if (hasLoggedIn) {
                setResult(RESULT_OK)
                finish()
            }
        }
    }

    private fun setupSignInButton() {
        binding.signInButton.setOnClickListener {
            hideKeyboard()

            val email = binding.emailTextField.editText!!.text.toString()
            val password = binding.passwordTextField.editText!!.text.toString()

            mViewModel.loginWithEmailAndPassword(email, password)
        }
    }

    private fun setupForgotPasswordButtons() {
        binding.forgotPasswordButton.setOnClickListener {
            Log.d("LoginActivity", "setupForgotPasswordButtons : forgotPasswordButton.setOnClickListener()")
            val forgotPasswordIntent = Intent(this, ForgotPasswordActivity::class.java)
            forgotPasswordLauncher.launch(forgotPasswordIntent)
        }
    }

    private fun setupSignUpButton() {
        binding.createAccountButton.setOnClickListener {
            Log.d("LoginActivity", "setupSignUpButton : createAccountButton.setOnClickListener()")
            val signUpIntent = Intent(this, SignUpActivity::class.java)
            signUpResultLauncher.launch(signUpIntent)
        }
    }

    private fun finishIfResultOk(result: ActivityResult) {
        if (result.resultCode == RESULT_OK) {
            setResult(RESULT_OK)
            finish()
        }
    }
}
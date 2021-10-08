package com.example.tdcfirebaseapp.pages.login

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.databinding.ActivityLoginBinding
import com.example.tdcfirebaseapp.pages.login.viewmodels.LoginViewModel
import com.google.android.material.snackbar.Snackbar

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private lateinit var mViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
                setResult(Activity.RESULT_OK)
                finish()
            }
        }

        setupSignInButton()
    }

    private fun setupSignInButton() {
        binding.signInButton.setOnClickListener {
            hideKeyboard()

            val email = binding.emailTextField.editText!!.text.toString()
            val password = binding.passwordTextField.editText!!.text.toString()

            mViewModel.loginWithEmailAndPassword(email, password)
        }
    }


    private fun Activity.hideKeyboard() {
        hideKeyboard(currentFocus ?: View(this))
    }

    private fun Context.hideKeyboard(view: View) {
        val inputMethodManager = getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
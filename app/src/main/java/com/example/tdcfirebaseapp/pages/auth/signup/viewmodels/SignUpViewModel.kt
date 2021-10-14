package com.example.tdcfirebaseapp.pages.auth.signup.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdcfirebaseapp.pages.auth.shared.contracts.AuthContract
import com.example.tdcfirebaseapp.pages.auth.signup.repositories.SignUpRepository
import java.lang.Exception

class SignUpViewModel: ViewModel() {
    private val mIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val mHasSignedUp: MutableLiveData<Boolean> = MutableLiveData(false)
    private val mHasErrors: MutableLiveData<Exception> = MutableLiveData()

    private val mRepo = SignUpRepository.Instance

    fun isLoading(): LiveData<Boolean> = mIsLoading
    fun hasSignedUp(): LiveData<Boolean> = mHasSignedUp
    fun hasErrors(): LiveData<Exception> = mHasErrors

    fun signUpWithEmailAndPassword(email: String, password: String) {
        mIsLoading.value = true

        mRepo.signUpWithEmailAndPassword(email, password, object : AuthContract.LoginResultListener {
            override fun onSuccess() {
                mIsLoading.value = false
                mHasErrors.value = null

                mHasSignedUp.value = true
            }

            override fun onFailure(exception: Exception) {
                mIsLoading.value = false
                mHasErrors.value = exception
            }
        })
    }

    fun validateFields(email: String, password: String, confirmation: String): Boolean {
        if (email.isBlank() || password.isBlank()) {
            mHasErrors.value = Exception("Email and password cannot be blank")
            return false
        }

        if (password != confirmation) {
            mHasErrors.value = Exception("Password confirmation does not match")
            return false
        }

        mHasErrors.value = null
        return true
    }

    companion object {
        const val TAG = "SignUpViewModel"
    }
}
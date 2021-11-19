package com.example.tdcfirebaseapp.pages.auth.forgotpassword.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdcfirebaseapp.pages.auth.forgotpassword.repositories.ForgotPasswordRepository
import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts

class ForgotPasswordViewModel: ViewModel() {
    private val mIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val mHasSentEmail: MutableLiveData<Boolean> = MutableLiveData(false)
    private val mHasErrors: MutableLiveData<Exception> = MutableLiveData()

    private val mRepo = ForgotPasswordRepository.Instance

    fun isLoading(): LiveData<Boolean> = mIsLoading
    fun hasSentEmail(): LiveData<Boolean> = mHasSentEmail
    fun hasErrors(): LiveData<Exception> = mHasErrors

    fun sendPasswordResetEmail(email: String) {
        mIsLoading.value = true

        mRepo.sendPasswordResetEmail(email, object : ViewModelContracts.ResultListener {
            override fun onSuccess() {
                mIsLoading.value = false
                mHasErrors.value = null
                mHasSentEmail.value = true
            }

            override fun onFailure(exception: java.lang.Exception) {
                Log.e(TAG, "sendPasswordResetEmail:failure $exception")

                mIsLoading.value = false
                mHasErrors.value = exception
            }
        })
    }

    companion object {
        const val TAG = "ForgotPasswordViewModel"
    }
}
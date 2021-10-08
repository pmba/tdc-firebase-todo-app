package com.example.tdcfirebaseapp.pages.login.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdcfirebaseapp.pages.login.contracts.AuthContract
import com.example.tdcfirebaseapp.pages.login.repositories.AuthRepository

class LoginViewModel: ViewModel() {
    private val mIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)
    private val mHasLoggedIn: MutableLiveData<Boolean> = MutableLiveData(false)
    private val mHasErrors: MutableLiveData<Exception> = MutableLiveData()

    private val mRepo = AuthRepository.Instance

    fun isLoading(): LiveData<Boolean> = mIsLoading
    fun hasLoggedIn(): LiveData<Boolean> = mHasLoggedIn
    fun hasErrors(): LiveData<Exception> = mHasErrors

    fun loginWithEmailAndPassword(email: String, password: String) {
        mIsLoading.value = true

        mRepo.loginWithEmailAndPassword(email, password, object : AuthContract.LoginResultListener {
            override fun onSuccess() {
                mIsLoading.value = false
                mHasErrors.value = null
                mHasLoggedIn.value = true
            }

            override fun onFailure(exception: Exception) {
                Log.w(TAG, "loginWithEmailAndPassword:failure $exception")

                mIsLoading.value = false
                mHasErrors.value = exception
            }
        })
    }

    companion object {
        const val TAG = "LoginViewModel"
    }
}
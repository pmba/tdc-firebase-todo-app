package com.example.tdcfirebaseapp.pages.profile.viewmodels

import android.app.Activity
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdcfirebaseapp.R
import com.example.tdcfirebaseapp.pages.profile.repositories.ProfileRepository
import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts

typealias OnInitListener = (exception: Exception?) -> Unit

class ProfileViewModel : ViewModel() {
    private val mUserName: MutableLiveData<String> = MutableLiveData()
    private val mUserEmail: MutableLiveData<String> = MutableLiveData()

    private val mErrorOccurred: MutableLiveData<Exception> = MutableLiveData()

    private val mRepo = ProfileRepository.Instance

    fun getName(): LiveData<String> = mUserName
    fun getEmail(): LiveData<String> = mUserEmail

    fun init(activity: Activity, onInitialized: OnInitListener) {
        try {
            val user = mRepo.getUser()

            mUserName.value = if (user.displayName.isNullOrBlank()) {
                activity.getString(R.string.anonimous_string)
            } else user.displayName

            mUserEmail.value = user.email!!

            mErrorOccurred.value = null
            onInitialized(null)
        } catch (exception: NullPointerException) {
            mErrorOccurred.value = exception
            onInitialized(exception)
        }
    }

    fun updateUserName(name: String) {
        mRepo.updateUserName(name, object : ViewModelContracts.ResultListener {
            override fun onSuccess() {
                mErrorOccurred.value = null
                mUserName.postValue(name)
            }

            override fun onFailure(exception: java.lang.Exception) {
                mErrorOccurred.value = exception
            }
        })
    }

    fun logout(listener: ViewModelContracts.ResultListener) {
        try {
            mRepo.logout()
            listener.onSuccess()
        } catch (e: Exception) {
            listener.onFailure(e)
        }
    }

    companion object {
        const val TAG = "ProfileViewModel"
    }
}
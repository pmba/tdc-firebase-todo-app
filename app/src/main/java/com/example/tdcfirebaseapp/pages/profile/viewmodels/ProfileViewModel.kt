package com.example.tdcfirebaseapp.pages.profile.viewmodels

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
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
    private val mUserProfileImg: MutableLiveData<Uri?> = MutableLiveData()

    private val mIsUploadingImage: MutableLiveData<Boolean> = MutableLiveData(false)

    private val mErrorOccurred: MutableLiveData<Exception> = MutableLiveData()

    private val mRepo = ProfileRepository.Instance

    fun getName(): LiveData<String> = mUserName
    fun getEmail(): LiveData<String> = mUserEmail
    fun getProfileImage(): LiveData<Uri?> = mUserProfileImg
    fun getIsUploadingImage(): LiveData<Boolean> = mIsUploadingImage

    fun init(activity: Activity, onInitialized: OnInitListener) {
        try {
            val user = mRepo.getUser()

            mUserName.value = if (user.displayName.isNullOrBlank()) {
                activity.getString(R.string.anonimous_string)
            } else user.displayName

            mUserEmail.value = user.email!!

            mUserProfileImg.value = user.photoUrl

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

    fun uploadProfileImage(bitmap: Bitmap) {
        mIsUploadingImage.value = true

        mRepo.uploadProfileImage(bitmap, object : ViewModelContracts.ResultListener {
            override fun onSuccess() {
                mIsUploadingImage.postValue(false)
                mErrorOccurred.postValue(null)
            }

            override fun onFailure(exception: java.lang.Exception) {
                mIsUploadingImage.postValue(false)
                mErrorOccurred.postValue(exception)
            }
        })
    }

    companion object {
        const val TAG = "ProfileViewModel"
    }
}
package com.example.tdcfirebaseapp.pages.profile.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdcfirebaseapp.pages.auth.login.repositories.LoginRepository
import com.example.tdcfirebaseapp.pages.profile.repositories.ProfileRepository
import com.example.tdcfirebaseapp.shared.contracts.ViewModelContracts

class ProfileViewModel : ViewModel() {
    private val mUserName: MutableLiveData<String> = MutableLiveData()
    private val mUserEmail: MutableLiveData<String> = MutableLiveData()

    private val mRepo = ProfileRepository.Instance

    fun getName(): LiveData<String> = mUserName
    fun getEmail(): LiveData<String> = mUserEmail

    fun init() {
        mUserName.value = "Lorem Ipsum"
        mUserEmail.value = "loremipsum@gmail.com"
    }

    fun updateUserName(name: String) {
        // Todo: Save to database
        mUserName.value = name
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
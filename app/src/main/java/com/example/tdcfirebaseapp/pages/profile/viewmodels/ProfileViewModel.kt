package com.example.tdcfirebaseapp.pages.profile.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    private val mUserName: MutableLiveData<String> = MutableLiveData()
    private val mUserEmail: MutableLiveData<String> = MutableLiveData()

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

    fun logout() {
        TODO()
    }

    companion object {
        const val TAG = "ProfileViewModel"
    }
}
package com.example.tdcfirebaseapp.pages.tasks.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.example.tdcfirebaseapp.pages.tasks.repositories.TaskRepository

class TaskViewModel : ViewModel() {
    private val mIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private var mTasks: MutableLiveData<List<Task>>? = null

    private val mRepo = TaskRepository.Instance

    fun init() {
        if (mTasks != null) {
            return
        }

        mTasks = mRepo.getTasks()
    }

    fun isLoading(): LiveData<Boolean> = mIsLoading
    fun getTasks(): LiveData<List<Task>> = mTasks!!
}
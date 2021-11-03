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

    fun addNewTask(task: Task) {
        mIsLoading.value = true

        val currentTasks = mRepo.addNewTask(task)
        mTasks!!.value = currentTasks
        mIsLoading.value = false
    }

    fun removeTask(uid: String) {
        mIsLoading.value = true

        val currentTasks = mRepo.removeTask(uid)
        mTasks!!.value = currentTasks

        mIsLoading.value = false
    }

    fun updateTask(uid: String, task: Task) {
        mIsLoading.value = true

        val currentTasks = mRepo.updateTask(uid, task)
        mTasks!!.value = currentTasks

        mIsLoading.value = false
    }
}
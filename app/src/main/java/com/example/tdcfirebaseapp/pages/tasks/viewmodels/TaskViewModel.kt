package com.example.tdcfirebaseapp.pages.tasks.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.example.tdcfirebaseapp.pages.tasks.repositories.TaskRepository
import com.example.tdcfirebaseapp.pages.tasks.repositories.TaskRepository.Instance.TaskType

class TaskViewModel : ViewModel() {
    private val mIsLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    private var mTasksData: MutableLiveData<ArrayList<Task>> = MutableLiveData(ArrayList())

    private val mRepo = TaskRepository.Instance

    fun init(typesToLoad: TaskType = TaskType.ALL) {
        if (mTasksData != null) {
            return
        }

        /* mTasks = mRepo.getTasks(typesToLoad) { tasksList ->
            Log.d("TaskViewModel", tasksList.toString())

            mTasks.value.
            mTasks!!.postValue(tasksList)
        } */

        mRepo.getTasks(typesToLoad) { tasksList ->
            Log.d("TaskViewModel", tasksList.toString())

            mTasksData.value!!.let { tasks ->
                tasks.clear()
                tasks.addAll(tasksList)
            }

            mTasksData.postValue(mTasksData.value)
        }
    }

    fun isLoading(): LiveData<Boolean> = mIsLoading
    fun getTasks(): LiveData<ArrayList<Task>> = mTasksData

    fun addNewTask(task: Task) {
        mIsLoading.value = true

        val currentTasks = mRepo.addNewTask(task)
        mTasksData.value = currentTasks
        mIsLoading.value = false
    }

    fun removeTask(uid: String) {
        mIsLoading.value = true

        val currentTasks = mRepo.removeTask(uid)
        mTasksData.value = currentTasks

        mIsLoading.value = false
    }

    fun updateTask(uid: String, task: Task) {
        mIsLoading.value = true

        val currentTasks = mRepo.updateTask(uid, task)
        mTasksData.value = currentTasks

        mIsLoading.value = false
    }
}
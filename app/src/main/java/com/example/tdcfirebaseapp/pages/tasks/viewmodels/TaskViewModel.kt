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

    companion object {
        const val TAG = "TaskViewModel"
    }

    fun isLoading(): LiveData<Boolean> = mIsLoading
    fun getTasks(): LiveData<ArrayList<Task>> = mTasksData

    fun init(typesToLoad: TaskType = TaskType.ALL) {
        mIsLoading.value = true

        mRepo.getTasks(typesToLoad) { tasksList ->
            Log.d(TAG, tasksList.toString())

            mTasksData.value!!.let { tasks ->
                tasks.clear()
                tasks.addAll(tasksList)
            }

            mTasksData.postValue(mTasksData.value)
            mIsLoading.postValue(false)
        }
    }

    fun addNewTask(userTask: Task) {
        mIsLoading.value = true

        mRepo.addNewTask(userTask) { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "An error occurred while creating new task : ${task.exception}")
            }

            mIsLoading.postValue(false)
        }
    }

    fun removeTask(uid: String) {
        mIsLoading.value = true

        mRepo.removeTask(uid) { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "An error occurred while deleting task(uid=$uid) : ${task.exception}")
            }

            mIsLoading.postValue(false)
        }
    }

    fun updateTask(uid: String, userTask: Task) {
        mIsLoading.value = true

        mRepo.updateTask(uid, userTask) { task ->
            if (!task.isSuccessful) {
                Log.e(TAG, "An error occurred while updating task(uid=$uid) : ${task.exception}")
            }

            mIsLoading.postValue(false)
        }
    }

    fun updateTaskState(uid: String, done: Boolean) {
        mRepo.updateTaskState(uid, done) { exception ->
            Log.e(TAG, "An error occurred while updating task(uid=$uid) state to (done=$done): " +
                    "$exception")
        }
    }
}
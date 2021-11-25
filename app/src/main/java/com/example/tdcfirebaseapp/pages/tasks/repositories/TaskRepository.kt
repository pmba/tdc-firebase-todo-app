package com.example.tdcfirebaseapp.pages.tasks.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.OnFailureListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class TaskRepository {
    companion object {
        const val TAG = "TaskRepository"
    }

    object Instance {
        private val mFbUser = FirebaseAuth.getInstance().currentUser!!
        private val mFbDatabase = FirebaseDatabase.getInstance()

        private val mDbTaskRef = mFbDatabase.reference.child("tasks").child(mFbUser.uid)

        private var mTasksList: ArrayList<Task> = ArrayList()

        fun getTasks(typesToLoad: TaskType, onGetTasks: (tasksList: ArrayList<Task>) -> Unit) {
            mDbTaskRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "getTasksFromRealtimeDatabase onDataChange: $snapshot")

                    val userTaskList = snapshot.children.map { taskSnap ->
                        taskSnap.getValue(Task::class.java)?.apply { uid = taskSnap.key!! }!!
                    }

                    mTasksList = ArrayList(userTaskList.filter { task ->
                        when(typesToLoad) {
                            TaskType.FINISHED -> task.done
                            TaskType.UNFINISHED -> !task.done
                            else -> true
                        }
                    })

                    onGetTasks(mTasksList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "getTasksFromRealtimeDatabase onCancelled: ${error.message}")
                }
            })
        }

        fun addNewTask(task: Task, onCompleteListener: OnCompleteListener<Void>) {
            mDbTaskRef.push().setValue(task).addOnCompleteListener(onCompleteListener)
        }

        fun removeTask(uid: String, onCompleteListener: OnCompleteListener<Void>) {
            mDbTaskRef.child(uid).removeValue().addOnCompleteListener(onCompleteListener)
        }

        fun updateTask(
            uid: String,
            task: Task,
            onCompleteListener: OnCompleteListener<Void>
        ) {
            mDbTaskRef.child(uid).setValue(task).addOnCompleteListener(onCompleteListener)
        }

        fun updateTaskState(
            uid: String,
            done: Boolean,
            onFailureListener: OnFailureListener
        ) {
            mDbTaskRef.child(uid).child("done").setValue(done)
                .addOnFailureListener(onFailureListener)
        }

        enum class TaskType {
            ALL, UNFINISHED, FINISHED
        }
    }
}
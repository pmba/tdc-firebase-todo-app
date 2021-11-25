package com.example.tdcfirebaseapp.pages.tasks.repositories

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.tdcfirebaseapp.pages.tasks.models.Task
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

        fun getTasks(
            typesToLoad: TaskType,
            onGetTasks: (tasksList: ArrayList<Task>) -> Unit
        ): MutableLiveData<List<Task>> {

            getTasksFromRealtimeDatabase(onGetTasks)
            loadTasksFromDatabase(typesToLoad)

            val data: MutableLiveData<List<Task>> = MutableLiveData()
            data.value = mTasksList

            return data
        }

        fun addNewTask(task: Task): ArrayList<Task> {
            mTasksList.add(task)
            return mTasksList
        }

        fun removeTask(uid: String): ArrayList<Task> {
            val taskToRemove = mTasksList.first { task -> task.uid == uid }
            mTasksList.remove(taskToRemove)
            return mTasksList
        }

        fun updateTask(uid: String, task: Task): ArrayList<Task> {
            val taskIndex = mTasksList.indexOfFirst { it.uid == uid }
            mTasksList[taskIndex] = task
            return mTasksList
        }

        private fun getTasksFromRealtimeDatabase(onGetTasks: (tasksList: ArrayList<Task>) -> Unit) {
            mDbTaskRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(TAG, "getTasksFromRealtimeDatabase onDataChange: $snapshot")

                    val userTaskList = snapshot.children.map { taskSnap ->
                        taskSnap.getValue(Task::class.java)?.apply { uid = taskSnap.key!! }!!
                    }

                    mTasksList = ArrayList(userTaskList)
                    onGetTasks(mTasksList)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "getTasksFromRealtimeDatabase onCancelled: ${error.message}")
                }
            })
        }

        /**
         * Função mocada para simular que os dados estão sendo pegos do realtime database
         */
        private fun loadTasksFromDatabase(typesToLoad: TaskType) {
            mTasksList.clear()

            if (typesToLoad == TaskType.ALL || typesToLoad == TaskType.FINISHED) {
                mTasksList.add(Task(
                    uid = UUID.randomUUID().toString(),
                    title = "Criar um método mocado para simular o realtime database",
                    done = true
                ))

                mTasksList.add(Task(
                    uid = UUID.randomUUID().toString(),
                    title = "Configurar a exibição das tasks",
                    done = true
                ))
            }

            if (typesToLoad == TaskType.ALL || typesToLoad == TaskType.UNFINISHED) {
                mTasksList.add(Task(
                    uid = UUID.randomUUID().toString(),
                    title = "Integrar o aplicativo com o realtime database",
                    done = false
                ))

                mTasksList.add(Task(
                    uid = UUID.randomUUID().toString(),
                    title = "Adicionar tasks ao banco de dados",
                    done = false
                ))

                mTasksList.add(Task(
                    uid = UUID.randomUUID().toString(),
                    title = "Carregar as tasks para o aplicativo",
                    done = false
                ))

                mTasksList.add(Task(
                    uid = UUID.randomUUID().toString(),
                    title = "Finalizar o aplicativo",
                    done = false
                ))

                mTasksList.add(Task(
                    uid = UUID.randomUUID().toString(),
                    title = "Apresentar o aplicativo",
                    done = false
                ))
            }
        }

        enum class TaskType {
            ALL, UNFINISHED, FINISHED
        }
    }
}
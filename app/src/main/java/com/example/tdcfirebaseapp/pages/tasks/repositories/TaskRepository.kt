package com.example.tdcfirebaseapp.pages.tasks.repositories

import androidx.lifecycle.MutableLiveData
import com.example.tdcfirebaseapp.pages.tasks.models.Task
import java.util.*
import kotlin.collections.ArrayList

class TaskRepository {
    object Instance {
        private val mTasksList: ArrayList<Task> = ArrayList()

        fun getTasks(typesToLoad: TaskType): MutableLiveData<List<Task>> {
            loadTasksFromDatabase(typesToLoad)

            val data: MutableLiveData<List<Task>> = MutableLiveData()
            data.value = mTasksList

            return data
        }

        fun addNewTask(task: Task): List<Task> {
            mTasksList.add(task)
            return mTasksList
        }

        fun removeTask(uid: String): List<Task> {
            val taskToRemove = mTasksList.first { task -> task.uid == uid }
            mTasksList.remove(taskToRemove)
            return mTasksList
        }

        fun updateTask(uid: String, task: Task): List<Task> {
            val taskIndex = mTasksList.indexOfFirst { it.uid == uid }
            mTasksList[taskIndex] = task
            return mTasksList
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
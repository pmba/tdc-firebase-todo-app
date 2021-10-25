package com.example.tdcfirebaseapp.pages.tasks.repositories

import androidx.lifecycle.MutableLiveData
import com.example.tdcfirebaseapp.pages.tasks.models.Task

class TaskRepository {
    object Instance {
        private val mTasksList: ArrayList<Task> = ArrayList()

        fun getTasks(): MutableLiveData<List<Task>> {
            loadTasksFromDatabase()

            val data: MutableLiveData<List<Task>> = MutableLiveData()
            data.value = mTasksList

            return data
        }

        /**
         * Função mocada para simular que os dados estão sendo pegos do realtime database
         */
        private fun loadTasksFromDatabase() {
            mTasksList.clear()

            mTasksList.add(Task(
                title = "Criar um método mocado para simular o realtime database",
                done = true
            ))

            mTasksList.add(Task(
                title = "Configurar a exibição das tasks",
                done = true
            ))

            mTasksList.add(Task(
                title = "Integrar o aplicativo com o realtime database",
                done = false
            ))

            mTasksList.add(Task(
                title = "Adicionar tasks ao banco de dados",
                done = false
            ))

            mTasksList.add(Task(
                title = "Carregar as tasks para o aplicativo",
                done = false
            ))

            mTasksList.add(Task(
                title = "Finalizar o aplicativo",
                done = false
            ))

            mTasksList.add(Task(
                title = "Apresentar o aplicativo",
                done = false
            ))
        }
    }
}
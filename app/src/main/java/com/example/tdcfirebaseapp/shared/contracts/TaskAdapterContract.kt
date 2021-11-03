package com.example.tdcfirebaseapp.shared.contracts

import com.example.tdcfirebaseapp.pages.tasks.models.Task

interface TaskAdapterContract {
    fun onEditRequest(task: Task)
}
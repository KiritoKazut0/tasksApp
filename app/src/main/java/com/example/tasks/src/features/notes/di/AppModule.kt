package com.example.tasks.src.features.notes.di

import com.example.tasks.src.core.network.http.RetrofitHelper
import com.example.tasks.src.features.notes.data.datasource.remote.TaskService
import com.example.tasks.src.features.notes.data.repository.TaskRepositoryImpl
import com.example.tasks.src.features.notes.domain.repository.TaskRepository
import com.example.tasks.src.features.notes.domain.usecase.CreateTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.DeleteTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.ListTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.UpdateTaskUseCase

object AppModule {


    private val taskService: TaskService by lazy {
        RetrofitHelper.getService(TaskService::class.java)
    }

    private val taskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(taskService)
    }


    val createTaskUseCase: CreateTaskUseCase by lazy {
        CreateTaskUseCase(taskRepository)
    }
    val listTaskUseCase: ListTaskUseCase by lazy {
        ListTaskUseCase(taskRepository)
    }
    val updateTaskUseCase: UpdateTaskUseCase by lazy {
        UpdateTaskUseCase(taskRepository)
    }
    val deleteTaskUseCase: DeleteTaskUseCase by lazy {
        DeleteTaskUseCase(taskRepository)
    }
}
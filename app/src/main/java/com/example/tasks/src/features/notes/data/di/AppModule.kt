package com.example.tasks.src.features.notes.data.di

import com.example.tasks.src.core.network.RetrofitHelper
import com.example.tasks.src.features.notes.data.datasource.TaskService
import com.example.tasks.src.features.notes.data.repository.TaskRepositoryImpl
import com.example.tasks.src.features.notes.domain.repository.TaskRepository
import com.example.tasks.src.features.notes.domain.usecase.CreateTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.DeleteTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.ListTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.UpdateTaskUseCase

object AppModule {
    private val api: TaskService = RetrofitHelper.retrofit.create(TaskService::class.java)

    private val repository: TaskRepository = TaskRepositoryImpl(api)

    val createTaskUseCase = CreateTaskUseCase(repository)
    val listTaskUseCase = ListTaskUseCase(repository)
    val updateTaskUseCase = UpdateTaskUseCase(repository)
    val deleteTaskUseCase = DeleteTaskUseCase(repository)
}
package com.example.tasks.src.features.notes.di

import com.example.tasks.src.core.di.NetworkModule
import com.example.tasks.src.core.di.RoomModule
import com.example.tasks.src.core.network.http.RetrofitHelper
import com.example.tasks.src.features.notes.data.datasource.remote.TaskService
import com.example.tasks.src.features.notes.data.datasource.remote.UploadFileService
import com.example.tasks.src.features.notes.data.repository.TaskLocalRepositoryImpl
import com.example.tasks.src.features.notes.data.repository.TaskRepositoryImpl
import com.example.tasks.src.features.notes.data.repository.UploadFileRepositoryImpl
import com.example.tasks.src.features.notes.data.repository.UploadLocalFileRepositoryImpl
import com.example.tasks.src.features.notes.domain.repository.TaskRepository
import com.example.tasks.src.features.notes.domain.repository.UploadFileRepository
import com.example.tasks.src.features.notes.domain.usecase.CreateTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.DeleteTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.ListTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.UpdateTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.UploadFileUseCase

object AppModule {

   private val networkManager = NetworkModule.networkManager

    private val taskService: TaskService by lazy {
        RetrofitHelper.getService(TaskService::class.java)
    }

    private val uploadFileService: UploadFileService by lazy {
        RetrofitHelper.getService(UploadFileService::class.java)
    }

    private val remoteTaskRepository: TaskRepository by lazy {
        TaskRepositoryImpl(taskService)
    }

    private val localTaskRepository: TaskRepository by lazy {
        val dao = RoomModule.getTaskDao()
        TaskLocalRepositoryImpl(dao)
    }

    private val remoteUploadFileRepository: UploadFileRepository by lazy {
        UploadFileRepositoryImpl(uploadFileService)
    }

    private val localUploadFileRepository: UploadFileRepository by lazy {
        UploadLocalFileRepositoryImpl()
    }

    val uploadFileUseCase: UploadFileUseCase by lazy {
        UploadFileUseCase(remoteUploadFileRepository, localUploadFileRepository, networkManager)
    }

    val createTaskUseCase: CreateTaskUseCase by lazy {
        CreateTaskUseCase(remoteTaskRepository, localTaskRepository, networkManager)
    }

    val listTaskUseCase: ListTaskUseCase by lazy {
        ListTaskUseCase(remoteTaskRepository, localTaskRepository, networkManager)
    }
    val updateTaskUseCase: UpdateTaskUseCase by lazy {
        UpdateTaskUseCase(remoteTaskRepository, localTaskRepository, networkManager)
    }
    val deleteTaskUseCase: DeleteTaskUseCase by lazy {
        DeleteTaskUseCase(remoteTaskRepository, localTaskRepository, networkManager)
    }
}
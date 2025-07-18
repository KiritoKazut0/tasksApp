package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.core.network.NetworkManager
import com.example.tasks.src.features.notes.domain.models.CreateTask
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.repository.TaskRepository

class CreateTaskUseCase(
    private val remoteRepository: TaskRepository,
    private val localRepository: TaskRepository,
    private val networkManager: NetworkManager
) {

    suspend operator fun invoke(request: CreateTask): Result<Task>{
        return if (networkManager.isConnected()){
            remoteRepository.create(request)
        } else{
            localRepository.create(request)
        }

    }

}
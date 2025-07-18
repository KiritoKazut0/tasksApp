package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.core.network.NetworkManager
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.models.UpdateTask
import com.example.tasks.src.features.notes.domain.repository.TaskRepository

class UpdateTaskUseCase (
    private val remoteRepository: TaskRepository,
    private val localRepository: TaskRepository,
    private val networkManager: NetworkManager
){

    suspend operator fun invoke(id: String ,request: UpdateTask): Result<Task>{
        return if (networkManager.isConnected()) {
            remoteRepository.update(id, request)
        } else {
            localRepository.update(id, request)
        }
    }

}
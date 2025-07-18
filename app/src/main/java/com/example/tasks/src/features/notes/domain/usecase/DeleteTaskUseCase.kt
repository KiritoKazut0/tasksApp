package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.core.network.NetworkManager
import com.example.tasks.src.features.notes.domain.repository.TaskRepository

class DeleteTaskUseCase(
    private val remoteRepository: TaskRepository,
    private val localRepository: TaskRepository,
    private val networkManager: NetworkManager
) {
    suspend operator fun invoke(id: String): Result<String>{
        return if (networkManager.isConnected()) {
            remoteRepository.delete(id)
        } else {
            localRepository.delete(id)
        }
    }
}
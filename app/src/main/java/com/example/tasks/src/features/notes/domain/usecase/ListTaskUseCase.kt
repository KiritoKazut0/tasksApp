package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.core.network.NetworkManager
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.repository.TaskRepository


class ListTaskUseCase(
    private val remoteRepository: TaskRepository,
    private val localRepository: TaskRepository,
    private val networkManager: NetworkManager
) {
    suspend operator fun invoke(id: String): Result<List<Task>> {
        return if (networkManager.isConnected()) {
            remoteRepository.list(id)
        } else {
            localRepository.list(id)
        }
    }
}

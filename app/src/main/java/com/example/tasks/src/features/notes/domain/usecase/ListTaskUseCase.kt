package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.repository.TaskRepository


class ListTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: String): Result<List<Task>> {
        return repository.list(id)
    }
}

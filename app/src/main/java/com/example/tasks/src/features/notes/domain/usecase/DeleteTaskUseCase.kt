package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.features.notes.domain.repository.TaskRepository

class DeleteTaskUseCase(private val repository: TaskRepository) {
    suspend operator fun invoke(id: String): String{
        return repository.delete(id)
    }
}
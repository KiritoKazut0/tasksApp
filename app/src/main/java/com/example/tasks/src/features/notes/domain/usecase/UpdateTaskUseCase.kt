package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.models.UpdateTask
import com.example.tasks.src.features.notes.domain.repository.TaskRepository

class UpdateTaskUseCase (private val repository: TaskRepository){

    suspend operator fun invoke(id: String ,request: UpdateTask): Task{
        return  repository.update(id, request)
    }

}
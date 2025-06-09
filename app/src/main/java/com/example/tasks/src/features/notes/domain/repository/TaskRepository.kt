package com.example.tasks.src.features.notes.domain.repository

import com.example.tasks.src.features.notes.domain.models.CreateTask
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.models.UpdateTask

interface TaskRepository {
    suspend fun list(idUser: String): List<Task>
    suspend fun create(request: CreateTask): Task
    suspend fun update(id: String ,request: UpdateTask): Task
    suspend fun delete(id: String): String
}
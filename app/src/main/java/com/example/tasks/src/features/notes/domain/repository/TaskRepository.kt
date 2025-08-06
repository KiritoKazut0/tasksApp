package com.example.tasks.src.features.notes.domain.repository

import com.example.tasks.src.features.notes.domain.models.CreateTask
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.models.UpdateTask

interface TaskRepository {
    suspend fun list(idUser: String): Result<List<Task>>
    suspend fun create(request: CreateTask): Result<Task>
    suspend fun update(id: String ,request: UpdateTask): Result<Task>
    suspend fun delete(id: String): Result<String>
}

interface TaskSyncRepository {
    suspend fun getPending(): Result<List<Task>>
    suspend fun markTaskAsSynced(id: String)
}

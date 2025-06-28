package com.example.tasks.src.features.notes.data.repository

import com.example.tasks.src.features.notes.data.datasource.remote.TaskService
import com.example.tasks.src.features.notes.domain.models.CreateTask
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.models.UpdateTask
import com.example.tasks.src.features.notes.domain.repository.TaskRepository


class TaskRepositoryImpl (private val api: TaskService): TaskRepository {

    override suspend fun list(idUser: String): Result<List<Task>> {
        return try {
            val response = api.listTask(idUser)
            Result.success(response.map { it.toDomain() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun create(request: CreateTask): Result<Task> {
      return  try {
            val response = api.createTask(request.toDo())
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun update(id: String, request: UpdateTask): Result<Task> {
      return  try {
            val response = api.updateTask(id, request.toDo())
            Result.success(response.toDomain())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(id: String): Result<String> {
       return try {
            val response = api.deleteTask(id)
            Result.success(response.message)
        } catch (e: Exception){
            Result.failure(e)
        }
    }

}
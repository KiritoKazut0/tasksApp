package com.example.tasks.src.features.notes.data.datasource

import com.example.tasks.src.features.notes.data.models.CreateTaskDto
import com.example.tasks.src.features.notes.data.models.DeleteTaskDto
import com.example.tasks.src.features.notes.data.models.ListTaskDto
import com.example.tasks.src.features.notes.data.models.TaskDto
import com.example.tasks.src.features.notes.data.models.UpdateTaskDto
import com.example.tasks.src.features.notes.domain.models.Task
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface TaskService {

    @GET("/tasks/{idUser}")
    suspend fun listTask(@Path("idUser") idUser: String): List<TaskDto>

    @POST("/tasks")
    suspend fun createTask(@Body request: CreateTaskDto): TaskDto

    @PATCH("/tasks/{id}")
    suspend fun updateTask(@Path("id") id: String, @Body request: UpdateTaskDto): TaskDto

    @DELETE("/tasks/{id}")
    suspend fun deleteTask(@Path("id") id: String): DeleteTaskDto


}
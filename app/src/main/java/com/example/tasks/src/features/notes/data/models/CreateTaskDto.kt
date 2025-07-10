package com.example.tasks.src.features.notes.data.models

import com.example.tasks.src.features.notes.domain.models.CreateTask


data class CreateTaskDto(
    val user_id: String,
    val titulo: String,
    val descripcion: String,
    val status: TaskStatusDto,
    val image: String
){
    fun toDomain() = CreateTask(
        user_id = user_id,
        titulo = titulo,
        descripcion = titulo,
        status = status.toDomain(),
        image = image
    )
}

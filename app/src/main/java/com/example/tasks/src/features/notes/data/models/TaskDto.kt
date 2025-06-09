package com.example.tasks.src.features.notes.data.models

import com.example.tasks.src.features.notes.domain.models.Task

data class TaskDto (
    val id: String,
    val titulo: String,
    val descripcion: String,
    val status: TaskStatusDto
) {

    fun toDomain() = Task(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        status = status.toDomain()
    )



}
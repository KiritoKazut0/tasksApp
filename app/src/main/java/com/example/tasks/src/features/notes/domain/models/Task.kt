package com.example.tasks.src.features.notes.domain.models

import com.example.tasks.src.features.notes.data.models.TaskDto

data class Task (
    val id: String,
    val titulo: String,
    val descripcion: String,
    val status: TaskStatus

) {
    fun toDo() = TaskDto(
        id = id,
        titulo = titulo,
        descripcion = descripcion,
        status = status.toDo()
    )
}
package com.example.tasks.src.features.notes.domain.models
import com.example.tasks.src.features.notes.data.models.UpdateTaskDto

data class UpdateTask(
    val titulo: String,
    val descripcion: String,
    val status: TaskStatus
){
    fun toDo() = UpdateTaskDto(
        titulo = titulo,
        descripcion = descripcion,
        status = status.toDo()
    )
}
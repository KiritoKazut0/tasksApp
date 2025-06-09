package com.example.tasks.src.features.notes.data.models



data class UpdateTaskDto(
    val titulo: String,
    val descripcion: String,
    val status: TaskStatusDto
)
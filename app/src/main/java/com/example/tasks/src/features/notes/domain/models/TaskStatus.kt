package com.example.tasks.src.features.notes.domain.models

import com.example.tasks.src.features.notes.data.models.TaskStatusDto

enum class TaskStatus {
    PENDIENTE,
    EN_PROGRESO,
    COMPLETADA,
    CANCELADA
}

fun TaskStatus.toDo(): TaskStatusDto = when (this) {
    TaskStatus.PENDIENTE -> TaskStatusDto.PENDIENTE
    TaskStatus.EN_PROGRESO -> TaskStatusDto.EN_PROGRESO
    TaskStatus.COMPLETADA -> TaskStatusDto.COMPLETADA
    TaskStatus.CANCELADA -> TaskStatusDto.CANCELADA
}
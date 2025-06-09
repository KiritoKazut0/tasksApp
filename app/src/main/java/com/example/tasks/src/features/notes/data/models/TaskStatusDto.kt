package com.example.tasks.src.features.notes.data.models

import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.google.gson.annotations.SerializedName

enum class TaskStatusDto {
    @SerializedName("pendiente")
    PENDIENTE,

    @SerializedName("en progreso")
    EN_PROGRESO,

    @SerializedName("completada")
    COMPLETADA,

    @SerializedName("cancelada")
    CANCELADA
}

fun  TaskStatusDto.toDomain(): TaskStatus = when(this){
    TaskStatusDto.EN_PROGRESO -> TaskStatus.EN_PROGRESO
    TaskStatusDto.PENDIENTE -> TaskStatus.PENDIENTE
    TaskStatusDto.COMPLETADA -> TaskStatus.COMPLETADA
    TaskStatusDto.CANCELADA -> TaskStatus.CANCELADA

}
package com.example.tasks.src.core.network.local.entities.converters

import androidx.room.TypeConverter
import com.example.tasks.src.features.notes.data.models.TaskStatusDto

class StatusConverter {

    @TypeConverter
    fun fromStatus(status: TaskStatusDto): String = status.name

    @TypeConverter
    fun toStatus(value: String): TaskStatusDto = when (value.uppercase()) {
        "PENDIENTE" -> TaskStatusDto.PENDIENTE
        "EN_PROGRESO" -> TaskStatusDto.EN_PROGRESO
        "COMPLETADA" -> TaskStatusDto.COMPLETADA
        "CANCELADA" -> TaskStatusDto.CANCELADA
        else -> throw IllegalArgumentException("Unknown status: $value")
    }
}

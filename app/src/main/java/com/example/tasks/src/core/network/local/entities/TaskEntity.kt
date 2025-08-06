package com.example.tasks.src.core.network.local.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.tasks.src.core.network.local.entities.converters.StatusConverter

import com.example.tasks.src.features.notes.data.models.TaskStatusDto


@Entity(tableName = "tasks")
@TypeConverters(StatusConverter::class)

data class TaskEntity(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") val id: String,

    @ColumnInfo(name = "id_user") val id_user: String,
    @ColumnInfo(name = "titulo") val titulo: String,
    @ColumnInfo(name = "descripcion") val descripcion: String,
    @ColumnInfo(name = "status") val status: TaskStatusDto,
    @ColumnInfo(name = "image") val image: String,
//    @ColumnInfo(name = "isSynced") val isSynced: Boolean = false
)







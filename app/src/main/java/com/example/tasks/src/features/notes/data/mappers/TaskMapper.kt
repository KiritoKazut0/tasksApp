package com.example.tasks.src.features.notes.data.mappers

import com.example.tasks.src.core.network.local.entities.TaskEntity
import com.example.tasks.src.features.notes.data.models.toDomain
import com.example.tasks.src.features.notes.domain.models.CreateTask
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.models.toDo
import java.util.UUID

fun TaskEntity.toDomain(): Task = Task(
    id, titulo, descripcion, status = status.toDomain(), image
)



fun CreateTask.toDatabase(userId: String): TaskEntity = TaskEntity(
    id = UUID.randomUUID().toString(),
    titulo = this.titulo,
    descripcion = this.descripcion,
    status = this.status.toDo(),
    image = this.image,
    id_user = userId

)
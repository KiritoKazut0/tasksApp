package com.example.tasks.src.features.notes.domain.models
import com.example.tasks.src.features.notes.data.models.CreateTaskDto

data class CreateTask(
    val user_id: String,
    val titulo: String,
    val descripcion: String,
    val status: TaskStatus,
    val image: String
) {
    fun toDo() = CreateTaskDto(
        user_id = user_id,
        titulo = titulo,
        descripcion = descripcion,
        status = status.toDo(),
        image = image

    )
}

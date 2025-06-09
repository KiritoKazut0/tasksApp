package com.example.tasks.src.features.auth.data.models
import com.example.tasks.src.features.auth.domain.models.User

data class UserDto (
    val id: String,
    val nombre: String,
    val correo: String
) {
    fun toDomain() = User(
        id = id,
        nombre = nombre,
        correo = correo
    )
}


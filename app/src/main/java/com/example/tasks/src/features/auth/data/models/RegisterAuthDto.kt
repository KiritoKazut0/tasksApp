package com.example.tasks.src.features.auth.data.models

import com.example.tasks.src.features.auth.domain.models.RegisterAuth

data class RegisterAuthDto (
    val nombre: String,
    val correo: String,
    val contraseña: String

){
    fun toDomain() = RegisterAuth(
        nombre = nombre,
        correo = correo,
        contraseña = contraseña
    )
}


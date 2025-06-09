package com.example.tasks.src.features.auth.domain.models

import com.example.tasks.src.features.auth.data.models.RegisterAuthDto

data class RegisterAuth(
    val nombre: String,
    val correo: String,
    val contraseña: String
){
    fun toDo() = RegisterAuthDto(nombre, correo, contraseña)
}
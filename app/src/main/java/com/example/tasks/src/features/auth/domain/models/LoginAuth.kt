package com.example.tasks.src.features.auth.domain.models

import com.example.tasks.src.features.auth.data.models.LoginAuthDto

data class LoginAuth(
    val correo: String,
    val contraseña : String
) {
    fun toDo() = LoginAuthDto(correo, contraseña)
}
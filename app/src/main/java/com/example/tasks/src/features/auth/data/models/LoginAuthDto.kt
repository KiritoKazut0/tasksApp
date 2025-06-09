package com.example.tasks.src.features.auth.data.models

import com.example.tasks.src.features.auth.domain.models.LoginAuth


data class LoginAuthDto(
    val correo: String,
    val contraseña : String
) {
    fun toDomain() = LoginAuth( correo = correo ,contraseña = contraseña)
}


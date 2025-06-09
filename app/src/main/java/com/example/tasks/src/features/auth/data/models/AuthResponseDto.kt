package com.example.tasks.src.features.auth.data.models

import com.example.tasks.src.features.auth.domain.models.AuthResponse


data class AuthResponseDto(
    val data: UserDto,
    val token: String
) {
    fun toDomain() = AuthResponse(
        data = data.toDomain(),
        token = token
    )
}

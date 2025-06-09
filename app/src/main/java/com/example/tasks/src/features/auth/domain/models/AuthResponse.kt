package com.example.tasks.src.features.auth.domain.models

data class AuthResponse(
val data: User,
val token : String
)
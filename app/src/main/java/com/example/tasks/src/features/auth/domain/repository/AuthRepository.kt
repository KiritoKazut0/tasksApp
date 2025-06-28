package com.example.tasks.src.features.auth.domain.repository

import com.example.tasks.src.features.auth.domain.models.LoginAuth
import com.example.tasks.src.features.auth.domain.models.AuthResponse
import com.example.tasks.src.features.auth.domain.models.RegisterAuth


interface AuthRepository{
    suspend fun access(request: LoginAuth) : Result<AuthResponse>
    suspend fun register(request: RegisterAuth): Result<AuthResponse>
}
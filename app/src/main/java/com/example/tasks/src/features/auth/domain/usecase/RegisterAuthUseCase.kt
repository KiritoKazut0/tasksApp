package com.example.tasks.src.features.auth.domain.usecase

import com.example.tasks.src.features.auth.domain.models.AuthResponse
import com.example.tasks.src.features.auth.domain.models.RegisterAuth
import com.example.tasks.src.features.auth.domain.repository.AuthRepository

class RegisterAuthUseCase(private val repository: AuthRepository) {

    suspend operator fun invoke(request: RegisterAuth): AuthResponse{
        return repository.register(request)
    }

}
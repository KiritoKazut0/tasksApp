package com.example.tasks.src.features.auth.domain.usecase

import com.example.tasks.src.features.auth.domain.models.LoginAuth
import com.example.tasks.src.features.auth.domain.models.AuthResponse
import com.example.tasks.src.features.auth.domain.repository.AuthRepository

class LoginAuthUseCase(private val repository: AuthRepository){
    suspend operator fun invoke(request: LoginAuth): AuthResponse{
        return repository.access(request)
    }
}
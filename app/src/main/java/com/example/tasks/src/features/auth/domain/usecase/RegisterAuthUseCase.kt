package com.example.tasks.src.features.auth.domain.usecase

import android.util.Log
import com.example.tasks.src.features.auth.domain.models.AuthResponse
import com.example.tasks.src.features.auth.domain.models.RegisterAuth
import com.example.tasks.src.features.auth.domain.repository.AuthRepository
import com.example.tasks.src.features.auth.domain.repository.TokenRepository

class RegisterAuthUseCase(
    private val authRepository: AuthRepository,
    private val tokenRepository: TokenRepository
) {

    suspend operator fun invoke(request: RegisterAuth): Result< AuthResponse>{
        val result = authRepository.register(request)
        Log.d("TOKEN", "Todo Ok")

        result.onSuccess { data ->
            tokenRepository.saveToken(data.token)
        }.onFailure {
            tokenRepository.saveToken("")
        }

        return result

    }

}
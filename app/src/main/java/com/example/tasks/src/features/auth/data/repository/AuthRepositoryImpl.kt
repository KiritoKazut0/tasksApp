package com.example.tasks.src.features.auth.data.repository

import com.example.tasks.src.features.auth.data.datasource.AuthService
import com.example.tasks.src.features.auth.domain.models.LoginAuth
import com.example.tasks.src.features.auth.domain.models.AuthResponse
import com.example.tasks.src.features.auth.domain.models.RegisterAuth
import com.example.tasks.src.features.auth.domain.repository.AuthRepository


class AuthRepositoryImpl (private val api: AuthService ): AuthRepository{
    override suspend fun access(request: LoginAuth): AuthResponse {
        val response =  api.accessUser(request.toDo())
        return response.toDomain()
    }

    override suspend fun register(request: RegisterAuth): AuthResponse {
       val  response =  api.registerUser(request.toDo())
        return response.toDomain()
    }


}
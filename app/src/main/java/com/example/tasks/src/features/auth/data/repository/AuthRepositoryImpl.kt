package com.example.tasks.src.features.auth.data.repository

import com.example.tasks.src.features.auth.data.datasource.AuthService
import com.example.tasks.src.features.auth.domain.models.LoginAuth
import com.example.tasks.src.features.auth.domain.models.AuthResponse
import com.example.tasks.src.features.auth.domain.models.RegisterAuth
import com.example.tasks.src.features.auth.domain.repository.AuthRepository


class AuthRepositoryImpl (private val api: AuthService ): AuthRepository{

    override suspend fun access(request: LoginAuth): Result<AuthResponse> {
        return try {
            val response =  api.accessUser(request.toDo())
            Result.success(response.toDomain())
        } catch (e: Exception){
            Result.failure(e)
        }
    }

    override suspend fun register(request: RegisterAuth): Result<AuthResponse>  {
        return try {
            val  response =  api.registerUser(request.toDo())
            Result.success(response.toDomain())
        } catch (e: Exception){
            Result.failure(e)
        }
    }


}
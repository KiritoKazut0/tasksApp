package com.example.tasks.src.features.auth.data.datasource


import com.example.tasks.src.features.auth.data.models.LoginAuthDto
import com.example.tasks.src.features.auth.data.models.AuthResponseDto
import com.example.tasks.src.features.auth.data.models.RegisterAuthDto
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthService {
    @POST("/auth/register")
    suspend fun registerUser(@Body request : RegisterAuthDto): AuthResponseDto

    @POST("/auth/login")
    suspend fun accessUser(@Body request: LoginAuthDto) : AuthResponseDto


}
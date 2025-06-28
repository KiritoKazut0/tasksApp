package com.example.tasks.src.features.auth.domain.repository

interface TokenRepository {
    suspend fun getToken(): String?
    suspend fun saveToken(token: String)
}
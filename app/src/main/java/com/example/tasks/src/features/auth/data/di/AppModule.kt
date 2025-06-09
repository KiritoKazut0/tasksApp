package com.example.tasks.src.features.auth.data.di

import com.example.tasks.src.core.network.RetrofitHelper
import com.example.tasks.src.features.auth.data.datasource.AuthService
import com.example.tasks.src.features.auth.data.repository.AuthRepositoryImpl
import com.example.tasks.src.features.auth.domain.repository.AuthRepository
import com.example.tasks.src.features.auth.domain.usecase.LoginAuthUseCase
import com.example.tasks.src.features.auth.domain.usecase.RegisterAuthUseCase

object AppModule{
    private val api: AuthService = RetrofitHelper.retrofit.create(AuthService::class.java)

    private val repository: AuthRepository = AuthRepositoryImpl(api)

    val accessUseCase = LoginAuthUseCase(repository)
    val registerUseCase = RegisterAuthUseCase(repository)



}
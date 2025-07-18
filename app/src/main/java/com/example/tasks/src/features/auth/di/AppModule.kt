package com.example.tasks.src.features.auth.di

import android.content.Context
import com.example.tasks.src.core.network.http.RetrofitHelper
import com.example.tasks.src.core.datastore.DataStoreManager
import com.example.tasks.src.features.auth.data.datasource.AuthService
import com.example.tasks.src.features.auth.data.repository.AuthRepositoryImpl
import com.example.tasks.src.features.auth.data.repository.TokenRepositoryImpl
import com.example.tasks.src.features.auth.domain.repository.AuthRepository
import com.example.tasks.src.features.auth.domain.repository.TokenRepository
import com.example.tasks.src.features.auth.domain.usecase.LoginAuthUseCase
import com.example.tasks.src.features.auth.domain.usecase.RegisterAuthUseCase

object AppModule{

    private lateinit var appContext: Context
    private lateinit var dataStoreManager: DataStoreManager

    private var isInitialized = false

    fun init(context: Context) {
        if (!isInitialized) {
            appContext = context.applicationContext
            dataStoreManager = DataStoreManager(appContext)
            RetrofitHelper.init(dataStoreManager)

            isInitialized = true
        }
    }


    private val tokenRepository: TokenRepository by lazy {
        TokenRepositoryImpl(dataStoreManager)
    }

    private val authService: AuthService by lazy {
        RetrofitHelper.getService(AuthService::class.java)
    }

    private val repositoryAuth: AuthRepository by lazy {
        AuthRepositoryImpl(authService)
    }



    val accessUseCase: LoginAuthUseCase by lazy {
        LoginAuthUseCase(
            authRepository = repositoryAuth,
            tokenRepository = tokenRepository
        )
    }

    val registerUseCase: RegisterAuthUseCase by lazy {
        RegisterAuthUseCase(
            authRepository =  repositoryAuth,
            tokenRepository = tokenRepository
        )
    }



}
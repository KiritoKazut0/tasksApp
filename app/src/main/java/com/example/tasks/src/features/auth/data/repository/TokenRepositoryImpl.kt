package com.example.tasks.src.features.auth.data.repository

import com.example.tasks.src.core.datastore.DataStoreManager
import com.example.tasks.src.core.datastore.PreferenceKeys
import com.example.tasks.src.features.auth.domain.repository.TokenRepository


class TokenRepositoryImpl(
    private val dataStoreToken: DataStoreManager
   ): TokenRepository {

    override suspend fun getToken(): String? = dataStoreToken.getKey(PreferenceKeys.TOKEN)
    override suspend fun saveToken(token: String) = dataStoreToken.saveKey(PreferenceKeys.TOKEN, token)

}
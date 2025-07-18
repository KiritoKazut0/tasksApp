package com.example.tasks.src.core.network.http.interceptor

import com.example.tasks.src.core.datastore.DataStoreManager
import com.example.tasks.src.core.datastore.PreferenceKeys
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Interceptor
import okhttp3.Response

class TokenCaptureInterceptor(
    private val dataStore: DataStoreManager
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val authHeader = response.header("Authorization")
        if (!authHeader.isNullOrEmpty()) {
            // Guardar el token en DataStore de forma segura
            CoroutineScope(Dispatchers.IO).launch {
                dataStore.saveKey(PreferenceKeys.TOKEN, authHeader)
            }
        }

        return response
    }

}
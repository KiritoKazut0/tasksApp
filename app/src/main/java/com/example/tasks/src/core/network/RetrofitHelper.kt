package com.example.tasks.src.core.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


object RetrofitHelper {
    private const val BaseUrl = "https://api.tasks.wildroid.space"

     val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BaseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }




}
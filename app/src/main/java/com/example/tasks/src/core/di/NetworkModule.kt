package com.example.tasks.src.core.di


import com.example.tasks.src.core.appcontext.AppContextHolder
import com.example.tasks.src.core.network.NetworkManager

object NetworkModule {
    val networkManager: NetworkManager by lazy {
        NetworkManager(AppContextHolder.get())
    }
}

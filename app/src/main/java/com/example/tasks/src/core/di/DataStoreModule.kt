package com.example.tasks.src.core.di


import com.example.tasks.src.core.appcontext.AppContextHolder
import com.example.tasks.src.core.datastore.DataStoreManager

object DataStoreModule {
    val dataStoreManager: DataStoreManager by lazy {
        DataStoreManager(AppContextHolder.get())
    }
}
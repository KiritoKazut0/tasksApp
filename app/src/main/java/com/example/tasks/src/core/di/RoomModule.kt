package com.example.tasks.src.core.di

import androidx.room.Room
import com.example.tasks.src.core.appcontext.AppContextHolder
import com.example.tasks.src.core.network.local.database.TaskDatabase

object RoomModule {
    private val dbInstance: TaskDatabase by lazy {
        Room.databaseBuilder(
            AppContextHolder.get(),
            TaskDatabase::class.java,
            "task_database"
        ).build()
    }

    fun getDatabase(): TaskDatabase = dbInstance
    fun getTaskDao() = dbInstance.getTaskDaou()

}

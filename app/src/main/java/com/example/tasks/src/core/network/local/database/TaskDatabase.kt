package com.example.tasks.src.core.network.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.tasks.src.core.network.local.dao.TaskDao
import com.example.tasks.src.core.network.local.entities.TaskEntity

@Database(entities = [TaskEntity::class], version = 1)

abstract class TaskDatabase: RoomDatabase() {
    abstract fun getTaskDaou(): TaskDao
}
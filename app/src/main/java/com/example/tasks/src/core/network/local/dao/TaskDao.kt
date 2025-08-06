package com.example.tasks.src.core.network.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.tasks.src.core.network.local.entities.TaskEntity

@Dao
interface TaskDao {

//    @Query("SELECT * FROM tasks WHERE isSynced = 0")
//    suspend fun getPendingTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id_user = :idUser")
    suspend fun listTask(idUser: String): List<TaskEntity>


    @Query("SELECT * FROM tasks WHERE id = :taskId")
    suspend fun getTaskById(taskId: String): TaskEntity?

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insert(task: TaskEntity)

    @Update
    suspend fun update(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE id = :taskId")
    suspend fun deleteById(taskId: String)

    @Query("DELETE FROM tasks")
    suspend fun deleteAll()



}

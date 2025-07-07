package com.example.tasks.src.core.hardware.domain

interface NotificationRepository {
    fun activeNotification(idTask: String, taskTitle: String)
}
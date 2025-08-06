package com.example.tasks.src.core.hardware.domain

interface VibratorRepository {
    fun vibrate(milliseconds: Long)
}
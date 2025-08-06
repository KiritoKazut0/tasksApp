package com.example.tasks.src.features.notes.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.src.core.hardware.domain.NotificationRepository
import com.example.tasks.src.core.hardware.domain.VibratorRepository
import com.example.tasks.src.features.notes.domain.repository.UploadFileRepository
import com.example.tasks.src.features.notes.domain.usecase.CreateTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.UploadFileUseCase
import com.example.tasks.src.features.notes.presentation.viewModel.CreateTaskViewModel

class CreateTaskViewModelFactory (
    private val createTaskUseCase: CreateTaskUseCase,
    private val notificationRepository: NotificationRepository,
    private val uploadFileUseCase: UploadFileUseCase,
    private val vibratorRepository: VibratorRepository
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)){
            return CreateTaskViewModel(
                createTaskUseCase,
                notificationRepository,
                uploadFileUseCase,
                vibratorRepository
                ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
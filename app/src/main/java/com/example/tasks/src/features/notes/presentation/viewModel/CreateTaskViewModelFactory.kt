package com.example.tasks.src.features.notes.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.src.features.notes.domain.usecase.CreateTaskUseCase

class CreateTaskViewModelFactory (
    private val createTaskUseCase: CreateTaskUseCase
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CreateTaskViewModel::class.java)){
            return CreateTaskViewModel(createTaskUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
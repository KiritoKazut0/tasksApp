package com.example.tasks.src.features.notes.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.src.features.notes.domain.usecase.UpdateTaskUseCase

class UpdateTaskViewModelFactory(
    private  val updateTaskUseCase: UpdateTaskUseCase
): ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UpdateTaskViewModel::class.java)){
            return UpdateTaskViewModel(updateTaskUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}
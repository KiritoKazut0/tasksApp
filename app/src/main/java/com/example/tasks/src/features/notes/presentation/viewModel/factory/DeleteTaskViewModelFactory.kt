package com.example.tasks.src.features.notes.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.src.features.notes.domain.usecase.DeleteTaskUseCase
import com.example.tasks.src.features.notes.presentation.viewModel.DeleteTaskViewModel

class DeleteTaskViewModelFactory (
    private val deleteTaskUseCase: DeleteTaskUseCase
): ViewModelProvider.Factory{
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteTaskViewModel::class.java)){
            return DeleteTaskViewModel(deleteTaskUseCase) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
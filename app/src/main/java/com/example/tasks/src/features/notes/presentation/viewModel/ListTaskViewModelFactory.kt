package com.example.tasks.src.features.notes.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.src.features.notes.domain.usecase.ListTaskUseCase


class ListTaskViewModelFactory (
    private val listTaskUseCase: ListTaskUseCase
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if (modelClass.isAssignableFrom(ListTaskViewModel::class.java)){
            return ListTaskViewModel(listTaskUseCase) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")

    }

}
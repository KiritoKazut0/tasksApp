package com.example.tasks.src.features.notes.presentation.viewModel.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.tasks.src.core.hardware.domain.CamaraRepository
import com.example.tasks.src.features.notes.presentation.viewModel.CameraViewModel

class CameraViewModelFactory(
private  val cameraRepository: CamaraRepository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

      return CameraViewModel(cameraRepository) as T
    }

}
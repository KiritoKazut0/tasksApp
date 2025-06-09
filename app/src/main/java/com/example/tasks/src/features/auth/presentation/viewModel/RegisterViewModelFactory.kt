    package com.example.tasks.src.features.auth.presentation.viewModel

    import androidx.lifecycle.ViewModel
    import androidx.lifecycle.ViewModelProvider
    import com.example.tasks.src.features.auth.domain.usecase.RegisterAuthUseCase

    class RegisterViewModelFactory(
        private val registerUseCase : RegisterAuthUseCase
    ): ViewModelProvider.Factory{
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
                return RegisterViewModel(registerUseCase) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }



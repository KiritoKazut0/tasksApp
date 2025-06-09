package com.example.tasks.src.features.notes.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.src.features.notes.domain.models.Task
import com.example.tasks.src.features.notes.domain.usecase.ListTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch



class ListTaskViewModel(private val listTaskUseCase: ListTaskUseCase) : ViewModel() {

    private val _tasks = MutableStateFlow<List<Task>>(emptyList())
    val tasks: StateFlow<List<Task>> = _tasks

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    fun removeTaskById(taskId: String) {
        _tasks.value = _tasks.value.filter { it.id != taskId }
    }



    fun fetchTasksForUser(userId: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val tasksResult = listTaskUseCase(userId)
                println("Tasks loaded for user $userId: $tasksResult")
                _tasks.value = tasksResult
                _errorMessage.value = null
            } catch (e: retrofit2.HttpException) {
                if (e.code() == 401) {
                    _errorMessage.value = "Sesión expirada. Por favor, inicia sesión nuevamente."
                } else {
                    _errorMessage.value = "Error al cargar las tareas. Código: ${e.code()}"
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error inesperado. Intenta de nuevo."
                println("Error loading tasks: ${e.message}")
            } finally {
                _isLoading.value = false
            }
        }
    }

}

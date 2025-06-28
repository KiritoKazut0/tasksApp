package com.example.tasks.src.features.notes.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.src.features.notes.domain.usecase.DeleteTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DeleteTaskViewModel(
    private val deleteTaskUseCase: DeleteTaskUseCase
) : ViewModel() {

    private val _deleteStatusMessage = MutableStateFlow("")
    val deleteStatusMessage: StateFlow<String> = _deleteStatusMessage

    fun deleteTask(taskId: String) {
        if (taskId.isBlank()) {
            _deleteStatusMessage.value = "Task ID is required"
            return
        }

        viewModelScope.launch {
            try {
                deleteTaskUseCase(taskId).getOrThrow()
                _deleteStatusMessage.value = "Task deleted successfully"
            } catch (e: Exception) {
                _deleteStatusMessage.value = e.message ?: "Unknown error"
            }
        }
    }
}

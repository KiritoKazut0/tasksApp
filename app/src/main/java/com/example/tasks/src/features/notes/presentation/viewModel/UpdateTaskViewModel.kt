package com.example.tasks.src.features.notes.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.example.tasks.src.features.notes.domain.models.UpdateTask
import com.example.tasks.src.features.notes.domain.usecase.UpdateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class UpdateTaskViewModel (
    private val updateTaskUseCase: UpdateTaskUseCase
): ViewModel(){
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _selectedStatus = MutableStateFlow(TaskStatus.PENDIENTE)
    val selectedStatus: StateFlow<TaskStatus> = _selectedStatus

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _success = MutableStateFlow(false)
    val success: StateFlow<Boolean> = _success

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isVisible = MutableStateFlow(false)
    val isVisible: StateFlow<Boolean> = _isVisible

    private val _titleExpanded = MutableStateFlow(false)
    val titleExpanded: StateFlow<Boolean> = _titleExpanded

    private val _descriptionExpanded = MutableStateFlow(false)
    val descriptionExpanded: StateFlow<Boolean> = _descriptionExpanded

    private val _showSuccess = MutableStateFlow(false)
    val showSuccess: StateFlow<Boolean> = _showSuccess


    fun onTitleChanged(newTitle: String) {
        _title.value = newTitle
    }

    fun onDescriptionChanged(newDescription: String) {
        _description.value = newDescription
    }

    fun onStatusSelected(newStatus: TaskStatus) {
        _selectedStatus.value = newStatus
    }

    fun setVisible(visible: Boolean) {
        _isVisible.value = visible
    }


    fun setShowSuccess(show: Boolean) {
        _showSuccess.value = show
    }

    fun updateTask(idTask: String){
        val taskUpdate = UpdateTask(
            titulo = _title.value,
            descripcion = _description.value,
            status = _selectedStatus.value
        )

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = updateTaskUseCase(idTask, taskUpdate).getOrThrow()
                _message.value = "Update task successful"
                _success.value = true
                _showSuccess.value = true
            } catch (e: Exception) {
                _message.value = e.message ?: "Unknown error"
                _success.value = false
                _showSuccess.value = false
            } finally {
                _isLoading.value = false
            }
        }

    }


}
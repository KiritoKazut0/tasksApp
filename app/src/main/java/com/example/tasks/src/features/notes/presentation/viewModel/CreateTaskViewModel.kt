package com.example.tasks.src.features.notes.presentation.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.src.features.notes.domain.models.CreateTask
import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.example.tasks.src.features.notes.domain.usecase.CreateTaskUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateTaskViewModel(
    private val createTaskUseCase: CreateTaskUseCase
) : ViewModel() {

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

    fun setTitleExpanded(expanded: Boolean) {
        _titleExpanded.value = expanded
    }

    fun setDescriptionExpanded(expanded: Boolean) {
        _descriptionExpanded.value = expanded
    }

    fun setShowSuccess(show: Boolean) {
        _showSuccess.value = show
    }

    private fun clearFields() {
        _title.value = ""
        _description.value = ""
        _selectedStatus.value = TaskStatus.PENDIENTE
    }

    fun validateFields(): Boolean {
        val currentTitle = _title.value
        val currentDescription = _description.value

        return when {
            currentTitle.isBlank() -> {
                _message.value = "Title cannot be empty"
                _success.value = false
                false
            }
            currentDescription.isBlank() -> {
                _message.value = "Description cannot be empty"
                _success.value = false
                false
            }
            else -> {
                _message.value = ""
                true
            }
        }
    }



    fun createTask(idUser: String) {

        if (_isLoading.value) return

        if (idUser.isBlank()) {
            _message.value = "User ID is required"
            _success.value = false
            return
        }

        _isLoading.value = true

        if (!validateFields()) {
            _message.value = "Porfavor completa todos los campos"
            _isLoading.value = false
            return
        }

        val task = CreateTask(
            user_id = idUser,
            titulo = _title.value,
            descripcion = _description.value,
            status = _selectedStatus.value
        )

        viewModelScope.launch {
            try {
                _isLoading.value = true
                val result = createTaskUseCase(task)
                _message.value = "User ID is required"
                _success.value = true
                _showSuccess.value = true
                clearFields()
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





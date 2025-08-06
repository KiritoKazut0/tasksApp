package com.example.tasks.src.features.notes.presentation.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tasks.src.core.hardware.domain.NotificationRepository
import com.example.tasks.src.core.hardware.domain.VibratorRepository
import com.example.tasks.src.features.notes.domain.models.CreateTask
import com.example.tasks.src.features.notes.domain.models.TaskStatus
import com.example.tasks.src.features.notes.domain.usecase.CreateTaskUseCase
import com.example.tasks.src.features.notes.domain.usecase.UploadFileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.File

class CreateTaskViewModel(
    private val createTaskUseCase: CreateTaskUseCase,
    private val notificationRepository: NotificationRepository,
    private val uploadFileUseCase: UploadFileUseCase,
    private val vibratorRepository: VibratorRepository
) : ViewModel() {

    // Estados del formulario
    private val _title = MutableStateFlow("")
    val title: StateFlow<String> = _title

    private val _description = MutableStateFlow("")
    val description: StateFlow<String> = _description

    private val _selectedStatus = MutableStateFlow(TaskStatus.PENDIENTE)
    val selectedStatus: StateFlow<TaskStatus> = _selectedStatus

    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage

    private val _showSuccess = MutableStateFlow(false)
    val showSuccess: StateFlow<Boolean> = _showSuccess

    // Estados adicionales para coincidir con UpdateTaskViewModel

    private val _isVisible = MutableStateFlow(false)
    val isVisible: StateFlow<Boolean> = _isVisible

    // Funciones para actualizar estado
    fun onTitleChanged(newTitle: String) {
        _title.value = newTitle
        clearError()
    }

    fun onDescriptionChanged(newDescription: String) {
        _description.value = newDescription
        clearError()
    }

    fun onStatusSelected(newStatus: TaskStatus) {
        _selectedStatus.value = newStatus
    }


    private fun clearError() {
        _errorMessage.value = null
    }

    fun clearSuccess() {
        _showSuccess.value = false
    }


    private fun validateFields(): Boolean {
        return when {
            _title.value.isBlank() -> {
                _errorMessage.value = "El título no puede estar vacío"
                false
            }
            _description.value.isBlank() -> {
                _errorMessage.value = "La descripción no puede estar vacía"
                false
            }
            else -> true
        }
    }


    fun createTask(userId: String, image: File?) {
        if (_isLoading.value) return
        if (!validateFields()) return

        if (image == null || !image.exists()) {
            _errorMessage.value = "No image selected"
            return
        }

        _isLoading.value = true
        _errorMessage.value = null

        viewModelScope.launch {
            try {
                val imageIsPublic = true;
                val urlImage = uploadFileUseCase(image, imageIsPublic).getOrThrow()
                Log.d("Image", urlImage.url)
                val task = CreateTask(
                    user_id = userId,
                    titulo = _title.value,
                    descripcion = _description.value,
                    status = _selectedStatus.value,
                    image = urlImage.url
                )



                val result = createTaskUseCase(task).getOrThrow()
                vibratorRepository.vibrate(500)
                notificationRepository.activeNotification(result.id, result.titulo)

                _showSuccess.value = true
                clearFields()
            } catch (e: Exception) {
                _errorMessage.value = e.message ?: "Error al crear la tarea"
            } finally {
                _isLoading.value = false
            }
        }
    }

    private fun clearFields() {
        _title.value = ""
        _description.value = ""
        _selectedStatus.value = TaskStatus.PENDIENTE
    }
}
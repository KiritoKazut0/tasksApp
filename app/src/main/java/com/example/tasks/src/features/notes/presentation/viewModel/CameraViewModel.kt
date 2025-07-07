package com.example.tasks.src.features.notes.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.tasks.src.core.hardware.domain.CamaraRepository
import java.io.File


class CameraViewModel(
    private val camaraRepository: CamaraRepository
) : ViewModel() {

    private val _showCamera = MutableStateFlow(false)
    val showCamera: StateFlow<Boolean> = _showCamera.asStateFlow()

    private val _capturedImages = MutableStateFlow<List<Uri>>(emptyList())
    val capturedImages: StateFlow<List<Uri>> = _capturedImages.asStateFlow()

    private val _isCapturing = MutableStateFlow(false)
    val isCapturing: StateFlow<Boolean> = _isCapturing.asStateFlow()

    private val _captureError = MutableStateFlow<String?>(null)
    val captureError: StateFlow<String?> = _captureError.asStateFlow()
    private val maxImages = 1

    // Funciones de control
    fun showCameraDialog() {
        if (canAddMoreImages()) {
            _showCamera.value = true
        }
    }

    fun hideCameraDialog() {
        _showCamera.value = false
    }

    fun takePhoto() {
        if (_isCapturing.value || !canAddMoreImages()) return

        viewModelScope.launch {
            _isCapturing.value = true
            _captureError.value = null

            camaraRepository.capturePhoto { uri ->
                _isCapturing.value = false

                if (uri != null) {
                    _capturedImages.value = _capturedImages.value + uri
                    _showCamera.value = false
                } else {
                    _captureError.value = "Error al capturar la foto"
                }
            }
        }
    }

    fun removeCapturedImage(index: Int) {
        _capturedImages.value = _capturedImages.value.toMutableList().also {
            if (index in it.indices) it.removeAt(index)
        }
    }


    // Helper functions
    fun canAddMoreImages(): Boolean {
        return _capturedImages.value.size < maxImages
    }


    fun getPhoto(): File? {
        return  camaraRepository.getSavedPhoto()
    }

    // Limpieza
    fun releaseCamera() {
        camaraRepository.cleanup()
    }

    override fun onCleared() {
        releaseCamera()
        super.onCleared()
    }
}
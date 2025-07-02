package com.example.tasks.src.features.notes.presentation.viewModel

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.tasks.src.core.hardware.domain.CamaraRepository

class CameraViewModel(
    private val camaraRepository: CamaraRepository
) : ViewModel() {

    private val _showCamera = MutableStateFlow(false)
    val showCamera: StateFlow<Boolean> = _showCamera.asStateFlow()

    private val _capturedImages = MutableStateFlow<List<Uri>>(emptyList())
    val capturedImages: StateFlow<List<Uri>> = _capturedImages.asStateFlow()

    private val _showImagePreview = MutableStateFlow<Uri?>(null)
    val showImagePreview: StateFlow<Uri?> = _showImagePreview.asStateFlow()

    private val _isCapturing = MutableStateFlow(false)
    val isCapturing: StateFlow<Boolean> = _isCapturing.asStateFlow()

    private val _captureError = MutableStateFlow<String?>(null)
    val captureError: StateFlow<String?> = _captureError.asStateFlow()

    private val maxImages = 1

    fun showCameraDialog() {
        if (canAddMoreImages()) {
            _showCamera.value = true
        }
    }

    fun hideCameraDialog() {
        _showCamera.value = false
    }


    fun takePhoto() {
        if (_isCapturing.value || !canAddMoreImages()) {
            return
        }

        viewModelScope.launch {
            _isCapturing.value = true
            _captureError.value = null

            camaraRepository.capturePhoto { uri ->
                _isCapturing.value = false

                if (uri != null) {
                    val currentImages = _capturedImages.value.toMutableList()
                    currentImages.add(uri)
                    _capturedImages.value = currentImages
                    _showCamera.value = false
                } else {
                    _captureError.value = "Error al capturar la foto"
                }
            }
        }
    }

    fun showImagePreview(imageUri: Uri) {
        _showImagePreview.value = imageUri
    }

    fun hideImagePreview() {
        _showImagePreview.value = null
    }


    fun removeCapturedImage(index: Int) {
        val currentImages = _capturedImages.value.toMutableList()
        if (index in 0 until currentImages.size) {
            currentImages.removeAt(index)
            _capturedImages.value = currentImages
        }
    }

    fun canAddMoreImages(): Boolean {
        return _capturedImages.value.size < maxImages
    }

    fun getCapturedImagesCount(): Int {
        return _capturedImages.value.size
    }

    fun clearError() {
        _captureError.value = null
    }

    fun releaseCamera(){
        camaraRepository.cleanup()
    }

    fun getAllCapturedImages(): List<Uri> {
        return _capturedImages.value
    }

    override fun onCleared() {
        super.onCleared()
    }
}
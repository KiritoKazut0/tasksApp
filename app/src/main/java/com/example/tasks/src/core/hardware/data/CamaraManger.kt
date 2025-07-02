package com.example.tasks.src.core.hardware.data

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.example.tasks.src.core.hardware.domain.CamaraRepository
import java.io.File
import java.util.UUID
import java.util.concurrent.Executor

class CamaraManager(
    private val context: Context,
    private val lifecycleOwner: LifecycleOwner,
    private val previewView: PreviewView
) : CamaraRepository {

    private val controller = LifecycleCameraController(context)
    private val executor: Executor = ContextCompat.getMainExecutor(context)
    private var isInitialized = false


    fun initializeCamera() {
        if (!isInitialized) {
            try {
                controller.setEnabledUseCases(
                    LifecycleCameraController.IMAGE_CAPTURE or
                            LifecycleCameraController.IMAGE_ANALYSIS
                )

                // Asignar controlador al PreviewView
                previewView.controller = controller

                // Vincular al lifecycle
                controller.bindToLifecycle(lifecycleOwner)
                isInitialized = true

            } catch (e: Exception) {
                Log.e( "Error inicializando cámara: ${e.message}", e.toString())
            }
        } else {
            Log.d("Error: ","Cámara ya inicializada")
        }
    }

    override fun capturePhoto(callback: (Uri?) -> Unit) {
        if (!isInitialized) {
            initializeCamera()
        }

        try {
            val internalDir = File(context.filesDir, "camera_photos")
            if (!internalDir.exists()) {
                internalDir.mkdirs()
            }

            val file = File(internalDir, "IMG_${UUID.randomUUID()}.jpg")
            val output = ImageCapture.OutputFileOptions.Builder(file).build()


            controller.takePicture(
                output,
                executor,
                object : ImageCapture.OnImageSavedCallback {
                    override fun onImageSaved(result: ImageCapture.OutputFileResults) {
                        val uri = result.savedUri ?: Uri.fromFile(file)
                        callback(uri)
                    }

                    override fun onError(exception: ImageCaptureException) {
                        callback(null)
                    }
                }
            )
        } catch (e: Exception) {
            callback(null)
        }
    }

    override fun getSavedPhoto(): List<File> {
        val internalDir = File(context.filesDir, "camera_photos")
        return if (internalDir.exists()) {
            internalDir.listFiles()?.toList() ?: emptyList()
        } else {
            emptyList()
        }
    }

   override fun cleanup() {
        if (isInitialized) {
            try {
                previewView.controller = null
                isInitialized = false
            } catch (e: Exception) {
                Log.e("Error limpiando recursos: ${e.message}", e.toString())
            }
        }
    }
}
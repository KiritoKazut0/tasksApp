package com.example.tasks.src.core.hardware.domain

import android.net.Uri
import java.io.File

interface CamaraRepository {
    fun capturePhoto(callback: (Uri?) -> Unit)
    fun getSavedPhoto(): File?
    fun cleanup()
}
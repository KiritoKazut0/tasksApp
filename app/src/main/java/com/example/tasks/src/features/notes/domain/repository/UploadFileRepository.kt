package com.example.tasks.src.features.notes.domain.repository

import com.example.tasks.src.features.notes.domain.models.UploadFile
import java.io.File


interface UploadFileRepository {
    suspend fun uploadImage(file: File, isPublic: Boolean): Result<UploadFile>
}
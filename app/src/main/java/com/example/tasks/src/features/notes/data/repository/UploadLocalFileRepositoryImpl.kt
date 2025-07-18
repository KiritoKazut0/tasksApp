package com.example.tasks.src.features.notes.data.repository

import com.example.tasks.src.features.notes.domain.models.UploadFile
import com.example.tasks.src.features.notes.domain.repository.UploadFileRepository
import java.io.File
import java.util.UUID

class UploadLocalFileRepositoryImpl (): UploadFileRepository {
    override suspend fun uploadImage(file: File, isPublic: Boolean): Result<UploadFile> {
        val local = UploadFile(
            url = file.absolutePath,
            isPublic = isPublic,
            key = UUID.randomUUID().toString()
        )
        return Result.success(local)
    }
}
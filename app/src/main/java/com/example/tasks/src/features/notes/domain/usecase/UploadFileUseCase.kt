package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.features.notes.domain.models.UploadFile
import com.example.tasks.src.features.notes.domain.repository.UploadFileRepository
import java.io.File

class UploadFileUseCase (private val repository: UploadFileRepository){
    suspend operator fun invoke(file: File): Result<UploadFile>{
        return repository.uploadImage(file)
    }

}
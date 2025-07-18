package com.example.tasks.src.features.notes.domain.usecase

import com.example.tasks.src.core.network.NetworkManager
import com.example.tasks.src.features.notes.domain.models.UploadFile
import com.example.tasks.src.features.notes.domain.repository.UploadFileRepository
import java.io.File

class UploadFileUseCase (
    private val remoteRepository: UploadFileRepository,
    private val localRepository: UploadFileRepository,
    private val networkManager: NetworkManager
){
    suspend operator fun invoke(file: File, isPublic: Boolean): Result<UploadFile>{
        return if (networkManager.isConnected()){
            remoteRepository.uploadImage(file, isPublic)
        } else{
            localRepository.uploadImage(file, isPublic)
        }
    }

}
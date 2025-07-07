package com.example.tasks.src.features.notes.data.repository

import com.example.tasks.src.features.notes.data.datasource.remote.UploadFileService
import com.example.tasks.src.features.notes.domain.models.UploadFile
import com.example.tasks.src.features.notes.domain.repository.UploadFileRepository
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File

class UploadFileRepositoryImpl( private  val api: UploadFileService): UploadFileRepository {

    override suspend fun uploadImage(file: File): Result<UploadFile> {
        return try {
            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
            val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
            val response = api.uploadImageFile(body)
            Result.success(response.toDomain())

        } catch (e: Exception){
            Result.failure(e)
        }
    }

}
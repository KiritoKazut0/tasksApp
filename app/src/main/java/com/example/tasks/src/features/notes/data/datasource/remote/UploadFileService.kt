package com.example.tasks.src.features.notes.data.datasource.remote

import com.example.tasks.src.features.notes.data.models.UploadFileDto
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface UploadFileService {
    @Multipart
    @POST("/upload/images")

    suspend fun uploadImageFile(
        @Part file: MultipartBody.Part,
        @Part("isPublic") isPublic: RequestBody
    ): UploadFileDto
}
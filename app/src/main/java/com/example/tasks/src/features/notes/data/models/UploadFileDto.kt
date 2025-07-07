package com.example.tasks.src.features.notes.data.models

import com.example.tasks.src.features.notes.domain.models.UploadFile

data class UploadFileDto(
    val url: String,
    val key: String,
    val isPublic: Boolean
){
    fun toDomain() = UploadFile(
        url = url,
        key = key,
        isPublic = isPublic
    )
}
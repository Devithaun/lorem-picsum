package com.devithaun.domain.repository

import com.devithaun.domain.model.Photo

interface PhotoRepository {
    suspend fun getPhotos(): Result<List<Photo>>
    suspend fun saveUserFilter(author: String)
    suspend fun getUserFilter(): String?
}
package com.devithaun.domain.usecase

import com.devithaun.domain.model.Photo
import com.devithaun.domain.repository.PhotoRepository

class GetPhotosUseCase constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(): List<Photo> = repository.getPhotos()
}
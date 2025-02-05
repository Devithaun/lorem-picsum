package com.devithaun.domain.usecase

import com.devithaun.domain.model.Photo
import com.devithaun.domain.repository.PhotoRepository
import javax.inject.Inject

class GetPhotosUseCase @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(): Result<List<Photo>> = repository.getPhotos()
}
package com.devithaun.domain.usecase

import com.devithaun.domain.repository.PhotoRepository

class SaveUserFilterUseCase constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(author: String) = repository.saveUserFilter(author)
}
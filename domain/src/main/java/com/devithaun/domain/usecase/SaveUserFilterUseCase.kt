package com.devithaun.domain.usecase

import com.devithaun.domain.repository.PhotoRepository
import javax.inject.Inject

class SaveUserFilterUseCase @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(author: String) = repository.saveUserFilter(author)
}
package com.devithaun.domain.usecase

import com.devithaun.domain.repository.PhotoRepository

class GetUserFilterUseCase constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(): String? = repository.getUserFilter()
}
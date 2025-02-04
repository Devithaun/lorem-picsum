package com.devithaun.domain.usecase

import com.devithaun.domain.repository.PhotoRepository
import javax.inject.Inject

class GetUserFilterUseCase @Inject constructor(private val repository: PhotoRepository) {
    suspend operator fun invoke(): String? = repository.getUserFilter()
}
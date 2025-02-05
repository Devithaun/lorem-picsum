package com.devithaun.domain

import com.devithaun.domain.model.Photo
import com.devithaun.domain.repository.PhotoRepository
import com.devithaun.domain.usecase.GetPhotosUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetPhotosUseCaseTest {

    @Mock
    private lateinit var repository: PhotoRepository

    private lateinit var useCase: GetPhotosUseCase


    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetPhotosUseCase(repository)
    }

    @Test
    fun onGetPhotos_onSuccess() = runTest {
        val mockPhotos = listOf(Photo("1", "Dave", "testURL"))
        Mockito.`when`(repository.getPhotos()).thenReturn(Result.success(mockPhotos))

        val result = useCase()

        Assert.assertEquals(Result.success(mockPhotos), result)
    }

    @Test
    fun onGetPhotos_onFailure() = runTest {
        val exception = Exception("Test Exception")
        Mockito.`when`(repository.getPhotos()).thenReturn(Result.failure(exception))

        val result = useCase()

        Assert.assertEquals(Result.failure<Exception>(exception), result)
    }
}
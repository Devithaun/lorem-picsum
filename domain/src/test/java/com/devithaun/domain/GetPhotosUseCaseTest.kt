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
    fun result_of_photos_returned_on_successful_invoke() = runTest {
        val mockPhotos = listOf(Photo("1", "Dave", "testURL"))
        Mockito.`when`(repository.getPhotos()).thenReturn(Result.success(mockPhotos))

        val result = useCase()

        Assert.assertEquals(Result.success(mockPhotos), result)
    }

    @Test
    fun result_of_failure_with_exception_returned_on_failed_invoke() = runTest {
        val exception = Exception("Test Exception")
        Mockito.`when`(repository.getPhotos()).thenReturn(Result.failure(exception))

        val result = useCase()

        Assert.assertEquals(Result.failure<Exception>(exception), result)
    }
}
package com.devithaun.domain

import com.devithaun.domain.repository.PhotoRepository
import com.devithaun.domain.usecase.GetUserFilterUseCase
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class GetUserFilterUseCaseTest {

    @Mock
    private lateinit var repository: PhotoRepository

    private lateinit var useCase: GetUserFilterUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        useCase = GetUserFilterUseCase(repository)
    }

    @Test
    fun author_filter_returned_on_successful_invoke() = runTest {
        Mockito.`when`(repository.getUserFilter()).thenReturn("Dave")

        val result = useCase.invoke()

        Assert.assertEquals("Dave", result)
    }

    @Test
    fun null_filter_returned_on_empty_filter() = runTest {
        Mockito.`when`(repository.getUserFilter()).thenReturn(null)

        val result = useCase.invoke()

        Assert.assertEquals(null, result)
    }
}
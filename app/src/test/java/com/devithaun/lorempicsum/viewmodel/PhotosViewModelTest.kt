package com.devithaun.lorempicsum.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devithaun.domain.model.Photo
import com.devithaun.domain.usecase.GetPhotosUseCase
import com.devithaun.domain.usecase.GetUserFilterUseCase
import com.devithaun.domain.usecase.SaveUserFilterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations


@OptIn(ExperimentalCoroutinesApi::class)
class PhotosViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = UnconfinedTestDispatcher()

    @Mock
    private lateinit var getUserFilterUseCase: GetUserFilterUseCase

    @Mock
    private lateinit var saveUserFilterUseCase: SaveUserFilterUseCase

    @Mock
    private lateinit var getPhotosUseCase: GetPhotosUseCase

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun onLoadingPhotosComplete_success_state_active() = runTest {
        val mockPhotos = listOf(Photo("1", "Dave", "testURL"))
        `when`(getPhotosUseCase())
            .thenReturn(Result.success(mockPhotos))

        val viewModel = PhotosViewModel(
            getPhotosUseCase,
            getUserFilterUseCase,
            saveUserFilterUseCase
        )

        assertEquals(PhotosViewModel.UiState.Success(mockPhotos), viewModel.uiState.value)
    }

    @Test
    fun onLoadingPhotosFailed_error_state_active() = runTest {
        val error = Exception("Network Error")
        `when`(getPhotosUseCase.invoke())
            .thenReturn(Result.failure(error))

        val viewModel = PhotosViewModel(
            getPhotosUseCase,
            getUserFilterUseCase,
            saveUserFilterUseCase
        )

        assertEquals(
            PhotosViewModel.UiState.Error("Failed to load photos. Please check your connection!"),
            viewModel.uiState.value
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }
}
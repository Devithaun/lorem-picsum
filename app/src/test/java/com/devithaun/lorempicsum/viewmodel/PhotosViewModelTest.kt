package com.devithaun.lorempicsum.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.devithaun.domain.usecase.GetPhotosUseCase
import com.devithaun.domain.usecase.GetUserFilterUseCase
import com.devithaun.domain.usecase.SaveUserFilterUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@OptIn(ExperimentalCoroutinesApi::class)
class PhotosViewModelTest {
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = StandardTestDispatcher()

    @Mock
    private lateinit var getUserFilterUseCase: GetUserFilterUseCase

    @Mock
    private lateinit var saveUserFilterUseCase: SaveUserFilterUseCase

    @Mock
    private lateinit var getPhotosUseCase: GetPhotosUseCase

    private lateinit var viewModel: PhotosViewModel

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        viewModel = PhotosViewModel(
            getPhotosUseCase,
            getUserFilterUseCase,
            saveUserFilterUseCase
        )
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun onLoadingPhotos_loading_state_active() = runTest {
        Mockito.`when`(getPhotosUseCase()).thenReturn(Result.success(emptyList()))

        val job = launch {
            viewModel.loadPhotos()
        }

        assertEquals(PhotosViewModel.UiState.Loading, viewModel.uiState.value)
        job.cancel()
    }
}
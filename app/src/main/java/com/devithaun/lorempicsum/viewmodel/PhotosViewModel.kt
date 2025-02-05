package com.devithaun.lorempicsum.viewmodel

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devithaun.domain.model.Photo
import com.devithaun.domain.usecase.GetPhotosUseCase
import com.devithaun.domain.usecase.GetUserFilterUseCase
import com.devithaun.domain.usecase.SaveUserFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val getUserFilterUseCase: GetUserFilterUseCase,
    private val saveUserFilterUseCase: SaveUserFilterUseCase
) : ViewModel() {

    sealed class UiState {
        data object Loading : UiState()
        data class Success(val photos: List<Photo>) : UiState()
        data class Error(val message: String) : UiState()
    }

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())

    private val _filter = MutableStateFlow<String?>(null)
    val filter: StateFlow<String?> = _filter

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState

    init {
        viewModelScope.launch {
            _filter.value = getUserFilterUseCase()
            combine(_photos, _filter) { photos, filter ->
                filterPhotos(photos, filter)
            }.collect { filteredPhotos ->
                _uiState.value = UiState.Success(filteredPhotos)
            }
        }
        loadPhotos()
    }

    fun loadPhotos() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            getPhotosUseCase()
                .onSuccess { photos ->
                    _photos.value = photos
                    _uiState.value = UiState.Success(filterPhotos(photos, _filter.value))
                }.onFailure {
                    _uiState.value =
                        UiState.Error("Failed to load photos. Please check your connection!")
                }
        }
    }

    fun retry() = loadPhotos()

    fun setFilter(filter: String) {
        viewModelScope.launch {
            saveUserFilterUseCase(filter)
            _filter.value = filter
        }
    }

    fun clearFilter() {
        viewModelScope.launch {
            saveUserFilterUseCase("")
            _filter.value = null
        }
    }

    private fun filterPhotos(photos: List<Photo>, filter: String?): List<Photo> {
        return if (filter.isNullOrEmpty()) photos else photos.filter { it.author == filter }
    }
}
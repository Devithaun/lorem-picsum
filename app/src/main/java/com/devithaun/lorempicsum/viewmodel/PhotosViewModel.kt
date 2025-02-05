package com.devithaun.lorempicsum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.devithaun.domain.model.Photo
import com.devithaun.domain.usecase.GetPhotosUseCase
import com.devithaun.domain.usecase.GetUserFilterUseCase
import com.devithaun.domain.usecase.SaveUserFilterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhotosViewModel @Inject constructor(
    private val getPhotosUseCase: GetPhotosUseCase,
    private val getUserFilterUseCase: GetUserFilterUseCase,
    private val saveUserFilterUseCase: SaveUserFilterUseCase
) : ViewModel() {

    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos: StateFlow<List<Photo>> = _photos

    private val _filter = MutableStateFlow<String?>(null)
    val filter: StateFlow<String?> = _filter

    private val _loading = MutableStateFlow(true)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isRetrying = MutableStateFlow(false)
    val isRetrying: StateFlow<Boolean> = _isRetrying

    init {
        viewModelScope.launch {
            _filter.value = getUserFilterUseCase()
            loadPhotos()
        }
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            _loading.value = true
            getPhotosUseCase()
                .onSuccess { photos ->
                    _error.value = null
                    _photos.value = if (_filter.value.isNullOrEmpty()) {
                        photos
                    } else {
                        photos.filter { it.author == _filter.value }
                    }
                }.onFailure {
                    _error.value = "Failed to load photos. Please check your connection!"
                }
            _loading.value = false
        }
    }

    fun retry() {
        viewModelScope.launch {
            _isRetrying.value = true
            getPhotosUseCase()
                .onSuccess { photos ->
                    _error.value = null
                    _photos.value = if (_filter.value.isNullOrEmpty()) {
                        photos
                    } else {
                        photos.filter { it.author == _filter.value }
                    }
                }.onFailure {
                    _error.value = "Failed to load photos. Please check your connection!"
                }
            _isRetrying.value = false
        }
    }

    fun setFilter(filter: String) {
        viewModelScope.launch {
            saveUserFilterUseCase(filter)
            _filter.value = filter
            loadPhotos()
        }
    }

    fun clearFilter() {
        viewModelScope.launch {
            saveUserFilterUseCase("")
            _filter.value = null
            loadPhotos()
        }
    }
}
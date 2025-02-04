package com.devithaun.lorempicsum.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    init {
        viewModelScope.launch {
            _filter.value = getUserFilterUseCase()
            loadPhotos()
        }
    }

    fun loadPhotos() {
        viewModelScope.launch {
            val photos = getPhotosUseCase()
            _photos.value = if (_filter.value.isNullOrEmpty()) {
                photos
            } else {
                photos.filter { it.author == _filter.value }
            }
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
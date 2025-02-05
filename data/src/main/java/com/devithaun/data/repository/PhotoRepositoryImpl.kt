package com.devithaun.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devithaun.data.db.PhotoDao
import com.devithaun.data.db.helper.toPhotoEntities
import com.devithaun.data.db.helper.toPhotos
import com.devithaun.data.network.PhotoApi
import com.devithaun.domain.model.Photo
import com.devithaun.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val api: PhotoApi,
    private val dataStore: DataStore<Preferences>,
    private val photoDao: PhotoDao
) : PhotoRepository {

    companion object {
        private val KEY = stringPreferencesKey("filter")
    }

    override suspend fun getPhotos(): Result<List<Photo>> {
        val cachedPhotos = photoDao.getAllPhotos()

        return if (cachedPhotos.isNotEmpty()) {
            Result.success(cachedPhotos.toPhotos())
        } else {
            loadFromApi()
        }
    }

    override suspend fun saveUserFilter(author: String) {
        dataStore.edit {
            it[KEY] = author
        }
    }

    override suspend fun getUserFilter(): String? {
        val prefs = dataStore.data.first()
        return prefs[KEY]
    }

    private suspend fun loadFromApi(): Result<List<Photo>> {
        return try {
            val photos = api.getPhotos()
            photoDao.insertPhotos(photos.toPhotoEntities())
            Result.success(photos)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
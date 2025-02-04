package com.devithaun.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.devithaun.data.network.PhotoApi
import com.devithaun.domain.model.Photo
import com.devithaun.domain.repository.PhotoRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class PhotoRepositoryImpl @Inject constructor(
    private val api: PhotoApi,
    private val dataStore: DataStore<Preferences>
) : PhotoRepository {

    companion object {
        private val KEY = stringPreferencesKey("filter")
    }

    override suspend fun getPhotos(): List<Photo> = api.getPhotos()

    override suspend fun saveUserFilter(author: String) {
        dataStore.edit {
            it[KEY] = author
        }
    }

    override suspend fun getUserFilter(): String? {
        val prefs = dataStore.data.first()
        return prefs[KEY]
    }
}
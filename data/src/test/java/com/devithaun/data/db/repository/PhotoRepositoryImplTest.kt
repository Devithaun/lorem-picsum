package com.devithaun.data.db.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.devithaun.data.db.PhotoDao
import com.devithaun.data.db.PhotoEntity
import com.devithaun.data.db.helper.toPhotoEntities
import com.devithaun.data.db.helper.toPhotos
import com.devithaun.data.network.PhotoApi
import com.devithaun.data.repository.PhotoRepositoryImpl
import com.devithaun.domain.model.Photo
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.doAnswer
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

class PhotoRepositoryImplTest {
    private lateinit var photoRepository: PhotoRepositoryImpl
    private val photoApi: PhotoApi = mock(PhotoApi::class.java)
    private val photoDao: PhotoDao = mock(PhotoDao::class.java)

    @Mock
    private lateinit var dataStore: DataStore<Preferences>

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)

        photoRepository = PhotoRepositoryImpl(photoApi, dataStore, photoDao)
    }

    @Test
    fun cached_photos_returned_when_present() = runTest {
        val cachedPhotos = listOf(PhotoEntity("1", "Dave", "http://dave.com/1"))
        `when`(photoDao.getAllPhotos()).thenReturn(cachedPhotos)

        val result = photoRepository.getPhotos()

        assertTrue(result.isSuccess)
        assertEquals(cachedPhotos.toPhotos(), result.getOrNull())
        verify(photoDao).getAllPhotos()
    }

    @Test
    fun load_photos_from_api_when_no_cached_photos() = runTest {
        val apiPhotos = listOf(Photo("1", "Dave", "http://dave.com/1"))
        val photoEntities = apiPhotos.toPhotoEntities()

        `when`(photoDao.getAllPhotos()).thenReturn(emptyList())
        `when`(photoApi.getPhotos()).thenReturn(apiPhotos)

        val result = photoRepository.getPhotos()

        assertTrue(result.isSuccess)
        assertEquals(apiPhotos, result.getOrNull())
        verify(photoDao).getAllPhotos()
        verify(photoApi).getPhotos()
        verify(photoDao).insertPhotos(photoEntities)
    }

    @Test
    fun failure_returned_when_exception_thrown() = runTest {
        val exception = Exception("An example exception")

        `when`(photoDao.getAllPhotos()).thenReturn(emptyList())
        doAnswer { throw exception }.`when`(photoApi).getPhotos()

        val result = photoRepository.getPhotos()

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
        verify(photoDao).getAllPhotos()
        verify(photoApi).getPhotos()
    }
}
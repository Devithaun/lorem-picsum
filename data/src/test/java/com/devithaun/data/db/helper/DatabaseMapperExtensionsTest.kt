package com.devithaun.data.db.helper

import com.devithaun.data.db.PhotoEntity
import com.devithaun.domain.model.Photo
import org.junit.Assert.assertEquals
import org.junit.Test

class DatabaseMapperExtensionsTest {

    @Test
    fun mapping_Photos_to_PhotoEntities() {
        val photos = listOf(
            Photo(id = "1", author = "Dave", url = "http://dave.com/1"),
            Photo(id = "2", author = "Jim", url = "http://jim.com/2")
        )

        val photoEntities = photos.toPhotoEntities()

        assertEquals(photos.size, photoEntities.size)
        assertEquals(photoEntities[0].id, photos[0].id)
        assertEquals(photoEntities[0].author, photos[0].author)
        assertEquals(photoEntities[0].url, photos[0].url)

        assertEquals(photoEntities[1].id, photos[1].id)
        assertEquals(photoEntities[1].author, photos[1].author)
        assertEquals(photoEntities[1].url, photos[1].url)
    }

    @Test
    fun mapping_PhotoEntities_to_Photos() {
        val photoEntities = listOf(
            PhotoEntity(id = "1", author = "Dave", url = "http://dave.com/1"),
            PhotoEntity(id = "2", author = "Jim", url = "http://jim.com/2")
        )

        val photos = photoEntities.toPhotos()

        assertEquals(photoEntities.size, photos.size)
        assertEquals(photos[0].id, photoEntities[0].id)
        assertEquals(photos[0].author, photoEntities[0].author)
        assertEquals(photos[0].url, photoEntities[0].url)

        assertEquals(photos[1].id, photoEntities[1].id)
        assertEquals(photos[1].author, photoEntities[1].author)
        assertEquals(photos[1].url, photoEntities[1].url)
    }
}
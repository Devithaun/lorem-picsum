package com.devithaun.data.db.helper

import com.devithaun.data.db.PhotoEntity
import com.devithaun.domain.model.Photo

fun List<Photo>.toPhotoEntities(): List<PhotoEntity> {
    return this.map { PhotoEntity(it.id, it.author, it.url) }
}

fun List<PhotoEntity>.toPhotos(): List<Photo> {
    return this.map { Photo(it.id, it.author, it.url) }
}
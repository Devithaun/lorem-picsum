package com.devithaun.data.network

import com.devithaun.domain.model.Photo
import retrofit2.http.GET

interface PhotoApi {

    @GET("v2/list")
    suspend fun getPhotos(): List<Photo>

    companion object {
        const val BASE_URL = "https://picsum.photos/"
    }
}
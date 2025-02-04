package com.devithaun.domain.model

import com.google.gson.annotations.SerializedName

data class Photo(
    val id: String,
    val author: String,
    @SerializedName("download_url") val url: String
)

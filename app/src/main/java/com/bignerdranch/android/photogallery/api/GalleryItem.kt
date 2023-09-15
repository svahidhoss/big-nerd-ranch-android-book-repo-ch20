package com.bignerdranch.android.photogallery.api

import com.squareup.moshi.Json

data class GalleryItem(
    val title: String,
    val id: String,
    @Json(name = "url_s") val url: String
)

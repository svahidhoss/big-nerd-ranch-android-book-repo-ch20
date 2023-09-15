package com.bignerdranch.android.photogallery.api

import retrofit2.http.GET

const val API_KEY = "597e12ab23165ecbe8e1192d625e9318"

interface FlickrApi {

    @GET(
        "services/rest/?method=flickr.interestingness.getList" +
                "&api_key=$API_KEY" +
                "&format=json" +
                "&nojsoncallback=1" +
                "&extras=url_s"
    )
    suspend fun fetchPhotos(): FlickrResponse
}

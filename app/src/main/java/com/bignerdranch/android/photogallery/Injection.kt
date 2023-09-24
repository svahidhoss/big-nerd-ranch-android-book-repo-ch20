package com.bignerdranch.android.photogallery

import com.bignerdranch.android.photogallery.api.FlickrApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

private const val BASE_URL = "https://api.flickr.com/"


/**
 * Class that handles object creation.
 * Like this, objects can be passed as parameters in the constructors and then replaced for
 * testing, where needed.
 */
object Injection {

    /**
     * Creates an instance of [FlickrApi].
     */
    private fun provideFlickrApi(): FlickrApi {
        // Kotlin reflection adapter
        val moshi = Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()

        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
        return retrofit.create(FlickrApi::class.java)
    }

    private var FLICKR_API_INSTANCE: FlickrApi? = null

    fun getFlickrApi(): FlickrApi {
        if (FLICKR_API_INSTANCE == null) {
            FLICKR_API_INSTANCE = provideFlickrApi()
        }
        return FLICKR_API_INSTANCE!!
    }
}
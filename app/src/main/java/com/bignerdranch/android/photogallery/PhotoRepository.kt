package com.bignerdranch.android.photogallery

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.bignerdranch.android.photogallery.api.FlickrApi
import com.bignerdranch.android.photogallery.api.GalleryItem
import com.bignerdranch.android.photogallery.paging.GalleryItemPagingSource
import kotlinx.coroutines.flow.Flow

private const val NETWORK_PAGE_SIZE = 100

class PhotoRepository(private var flickrApi: FlickrApi) {

    suspend fun fetchPhotos(page: Int = 1) = flickrApi.fetchPhotos(page)

    suspend fun searchPhotos(query: String, page: Int = 1) = flickrApi.searchPhotos(query, page)

    fun getSearchResultStream(query: String = ""): Flow<PagingData<GalleryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = NETWORK_PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { GalleryItemPagingSource(query) }
        ).flow
    }
}

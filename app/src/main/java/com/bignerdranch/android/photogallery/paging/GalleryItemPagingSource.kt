package com.bignerdranch.android.photogallery.paging

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.bignerdranch.android.photogallery.PhotoRepository
import com.bignerdranch.android.photogallery.api.GalleryItem
import java.lang.Exception

private const val STARTING_KEY = 1

private const val MAXIMUM_KEY = 5

private const val TAG = "GalleryItemPagingSource"

class GalleryItemPagingSource : PagingSource<Int, GalleryItem>() {

    private val photoRepository = PhotoRepository()
    override fun getRefreshKey(state: PagingState<Int, GalleryItem>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GalleryItem> {

        // Start paging with the STARTING_KEY if this is the first load
        val nextPageNumber = params.key ?: STARTING_KEY
        var items = emptyList<GalleryItem>()

        try {
            items = photoRepository.fetchPhotos(nextPageNumber)
            Log.d(TAG, "Items received: $items")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to fetch gallery items", e)
        }

        return LoadResult.Page(
            data = items,

            // Make sure we don't try to load items behind the STARTING_KEY
            prevKey = when (nextPageNumber) {
                STARTING_KEY -> null
                else -> nextPageNumber - 1
            },
            nextKey = if (nextPageNumber + 1 > MAXIMUM_KEY) null else nextPageNumber + 1
        )
    }
}

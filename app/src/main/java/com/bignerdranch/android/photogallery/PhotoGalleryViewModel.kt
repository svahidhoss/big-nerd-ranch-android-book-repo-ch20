package com.bignerdranch.android.photogallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bignerdranch.android.photogallery.api.GalleryItem
import kotlinx.coroutines.flow.Flow

private const val TAG = "PhotoGalleryViewModel"

class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository = PhotoRepository()

    val galleryItems: Flow<PagingData<GalleryItem>> =
        photoRepository.getSearchResultStream().cachedIn(viewModelScope)
}

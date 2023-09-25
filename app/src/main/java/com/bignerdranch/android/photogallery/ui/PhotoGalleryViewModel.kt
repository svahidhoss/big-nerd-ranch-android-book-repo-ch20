package com.bignerdranch.android.photogallery.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bignerdranch.android.photogallery.Injection
import com.bignerdranch.android.photogallery.PhotoRepository
import com.bignerdranch.android.photogallery.api.GalleryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

private const val TAG = "PhotoGalleryViewModel"

class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository = PhotoRepository(Injection.getFlickrApi())

    var galleryItems: Flow<PagingData<GalleryItem>> =
        photoRepository.getSearchResultStream().cachedIn(viewModelScope)

    private val _queryState = MutableStateFlow("")
    val queryState: StateFlow<String> = _queryState

    fun setQuery(query: String) {
        _queryState.value = query
    }

    fun fetchGalleryItems(query: String) {
        galleryItems = photoRepository.getSearchResultStream(query).cachedIn(viewModelScope)
    }
}

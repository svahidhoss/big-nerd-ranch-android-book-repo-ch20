package com.bignerdranch.android.photogallery.ui

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.bignerdranch.android.photogallery.Injection
import com.bignerdranch.android.photogallery.PhotoRepository
import com.bignerdranch.android.photogallery.PreferencesRepository
import com.bignerdranch.android.photogallery.api.GalleryItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.lang.Exception

private const val TAG = "PhotoGalleryViewModel"

class PhotoGalleryViewModel : ViewModel() {

    private val photoRepository = PhotoRepository(Injection.getFlickrApi())
    private val preferencesRepository = PreferencesRepository.get()

    private val _queryState = MutableStateFlow("")
    val queryState: StateFlow<String> = _queryState

    lateinit var galleryItems: Flow<PagingData<GalleryItem>>

    init {
        viewModelScope.launch {
            preferencesRepository.storedQuery.collectLatest { storedQuery ->
                try {
                    _queryState.value = storedQuery
                    galleryItems =
                        photoRepository.getSearchResultStream(storedQuery).cachedIn(viewModelScope)
                } catch (e: Exception) {
                    Log.e(TAG, "Failed to fetch gallery items", e)
                }
            }
        }
    }

    fun setQuery(query: String) {
        viewModelScope.launch { preferencesRepository.setStoredQuery(query) }
//        _queryState.value = query
    }

    fun fetchGalleryItems(query: String) {
        galleryItems = photoRepository.getSearchResultStream(query).cachedIn(viewModelScope)
    }
}

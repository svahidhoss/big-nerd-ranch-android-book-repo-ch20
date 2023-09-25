package com.bignerdranch.android.photogallery.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.PagingData
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.api.GalleryItem
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

private const val TAG = "PhotoGalleryFragment"

/**
 * A simple [Fragment] subclass.
 */
class PhotoGalleryFragment : Fragment() {
    private var _binding: FragmentPhotoGalleryBinding? = null

    private var searchView: SearchView? = null

    private val photoGalleryViewModel: PhotoGalleryViewModel by viewModels()

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)
        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    private var dataCollectionJob: Job? = null

    private fun startCollectingGalleryItems(pagingDataFlow: Flow<PagingData<GalleryItem>>) {
        dataCollectionJob = viewLifecycleOwner.lifecycleScope.launch {
            pagingDataFlow.collectLatest { items ->
                Log.d(TAG, "Response received: $items")
                (binding.photoGrid.adapter as PhotoListAdapter).submitData(items)
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.photoGrid.adapter = PhotoListAdapter()

        viewLifecycleOwner.lifecycleScope.launch {
            photoGalleryViewModel.uiState.collect { state ->
                searchView?.setQuery(state.query, false)
                dataCollectionJob?.cancel()
                (binding.photoGrid.adapter as PhotoListAdapter).refresh()
                // Update UI with new state
                startCollectingGalleryItems(state.pagingDataFlow)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.fragment_photo_gallery, menu)

        val searchItem = menu.findItem(R.id.menu_item_search)
        searchView = searchItem.actionView as? SearchView

        searchView?.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "QueryTextSubmit: $query")
                photoGalleryViewModel.setQuery(query ?: "")
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "QueryTextChange: $newText")
                return false
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_item_clear -> {
                photoGalleryViewModel.setQuery("")
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyOptionsMenu() {
        super.onDestroyOptionsMenu()
        searchView = null
    }
}

package com.bignerdranch.android.photogallery

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.bignerdranch.android.photogallery.api.FlickrApi
import com.bignerdranch.android.photogallery.databinding.FragmentPhotoGalleryBinding
import retrofit2.Retrofit

/**
 * A simple [Fragment] subclass.
 */
class PhotoGalleryFragment : Fragment() {
    private var _binding: FragmentPhotoGalleryBinding? = null

    private val binding
        get() = checkNotNull(_binding) {
            "Cannot access binding because it is null. Is the view visible?"
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPhotoGalleryBinding.inflate(inflater, container, false)
        binding.photoGrid.layoutManager = GridLayoutManager(context, 3)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val retrofit: Retrofit = Retrofit.Builder().baseUrl("https://www.flickr.com/").build()
        val flickrApi = retrofit.create(FlickrApi::class.java)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

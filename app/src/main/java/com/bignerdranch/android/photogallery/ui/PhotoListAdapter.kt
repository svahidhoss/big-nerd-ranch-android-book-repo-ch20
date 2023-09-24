package com.bignerdranch.android.photogallery.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bignerdranch.android.photogallery.R
import com.bignerdranch.android.photogallery.api.GalleryItem
import com.bignerdranch.android.photogallery.databinding.ListItemGalleryBinding

class PhotoListAdapter :
    PagingDataAdapter<GalleryItem, PhotoViewHolder>(ARTICLE_DIFF_CALLBACK) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListItemGalleryBinding.inflate(inflater, parent, false)
        return PhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        val item = getItem(position)
        item?.let { holder.bind(it) }
    }

    companion object {
        private val ARTICLE_DIFF_CALLBACK = object : DiffUtil.ItemCallback<GalleryItem>() {
            override fun areItemsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: GalleryItem, newItem: GalleryItem): Boolean =
                oldItem == newItem
        }
    }
}

class PhotoViewHolder(private val binding: ListItemGalleryBinding) :
    RecyclerView.ViewHolder(binding.root) {

    fun bind(galleryItem: GalleryItem) {
        binding.itemImageView.load(galleryItem.url) {
            placeholder(R.drawable.ic_downloading)
            crossfade(true)
        }
    }
}

package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.net.toUri
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.vinilos.R
import com.example.vinilos.databinding.AlbumItemBinding
import com.example.vinilos.models.Album

class AlbumsAdapter: RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {
    var albums : List<Album> = emptyList()
        set(value){
            val diffCallback = AlbumDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            try {
                diffResult.dispatchUpdatesTo(this)
            } catch (e: Exception) {
                // Skip notifications in unit tests
            }
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        val binding: AlbumItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.album_item,
            parent,
            false
        )
        return AlbumViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.album = albums[position]
        }
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    class AlbumViewHolder(val viewDataBinding: AlbumItemBinding) : RecyclerView.ViewHolder(viewDataBinding.root) {
        fun bind(album: Album) {
            Glide.with(itemView)
                .load(album.cover.toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                )
                .into(viewDataBinding.albumCover)
        }
    }

    private class AlbumDiffCallback(private val oldList: List<Album>, private val newList: List<Album>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = 
            oldList[oldItemPosition].albumId == newList[newItemPosition].albumId
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = 
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}

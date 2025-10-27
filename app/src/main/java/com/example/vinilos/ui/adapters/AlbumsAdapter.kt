package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.AlbumItemBinding
import com.example.vinilos.models.Album

class AlbumsAdapter: RecyclerView.Adapter<AlbumsAdapter.AlbumViewHolder>() {
    var albums : List<Album> = emptyList()
        set(value) {
            field = value
            notifyDataSetChanged()
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
        holder.bind(albums[position])
    }

    override fun getItemCount(): Int = albums.size

    class AlbumViewHolder(val binding: AlbumItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(album: Album) {
            binding.album = album
            
            Glide.with(binding.root.context)
                .load(album.cover)
                .placeholder(R.drawable.placeholder_album)
                .error(R.drawable.placeholder_album)
                .into(binding.albumCover)
                
            binding.executePendingBindings()
        }
    }
}
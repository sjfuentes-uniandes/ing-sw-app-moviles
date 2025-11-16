package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.ArtistItemBinding
import com.example.vinilos.models.Artist

class ArtistsAdapter(
    private val onArtistClick: (Artist) -> Unit
) : RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>() {

    var artists: List<Artist> = emptyList()
        set(value) {
            val oldList = field
            field = value
            val diffCallback = object : DiffUtil.Callback() {
                override fun getOldListSize(): Int = oldList.size
                override fun getNewListSize(): Int = value.size
                override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldList[oldItemPosition].artistId == value[newItemPosition].artistId
                override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
                    oldList[oldItemPosition] == value[newItemPosition]
            }
            DiffUtil.calculateDiff(diffCallback).dispatchUpdatesTo(this)
        }

    class ArtistViewHolder(val binding: ArtistItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(artist: Artist, onArtistClick: (Artist) -> Unit) {
            binding.artist = artist

            Glide.with(binding.root)
                .load(artist.image)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .centerCrop()
                .into(binding.artistImage)

            binding.root.setOnClickListener {
                onArtistClick(artist)
            }

            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val binding: ArtistItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.artist_item,
            parent,
            false
        )
        return ArtistViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]
        holder.bind(artist, onArtistClick)
    }

    override fun onViewRecycled(holder: ArtistViewHolder) {
        super.onViewRecycled(holder)
        Glide.with(holder.binding.root).clear(holder.binding.artistImage)
    }

    override fun getItemCount(): Int = artists.size
}

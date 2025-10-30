package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.ArtistItemBinding
import com.example.vinilos.models.Artist

class ArtistsAdapter :
    RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>() {

        var artists:List<Artist> = emptyList()
            set(value){
                field = value
                notifyDataSetChanged()
            }

    class ArtistViewHolder(val binding: ArtistItemBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(artist: Artist) {
            binding.artist = artist

            Glide.with(binding.root.context)
                .load(artist.image)
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.artistImage)

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

    /**
     * *poblar* una fila con datos.
     */
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]
        holder.bind(artist)
    }

    override fun getItemCount(): Int = artists.size
}
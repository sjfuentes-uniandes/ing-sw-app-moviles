package com.example.vinilos.ui.artists

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.databinding.ArtistItemBinding
import com.example.vinilos.models.Artist

class ArtistsAdapter(private var artists: List<Artist>) :
    RecyclerView.Adapter<ArtistsAdapter.ArtistViewHolder>() {

    class ArtistViewHolder(val binding: ArtistItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtistViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ArtistItemBinding.inflate(inflater, parent, false)
        return ArtistViewHolder(binding)
    }

    /**
     * *poblar* una fila con datos.
     */
    override fun onBindViewHolder(holder: ArtistViewHolder, position: Int) {
        val artist = artists[position]

        // Usando ViewBinding (binding) para acceder a las vistas
        holder.binding.artistName.text = artist.name

        // (Aquí iría la lógica para cargar la imagen con Glide o Picasso)
        // Glide.with(holder.itemView.context).load(artist.imageUrl).into(holder.binding.artistImage)

        // Lógica para el check de verificado
        // holder.binding.artistVerifiedCheck.visibility = if (artist.isVerified) View.VISIBLE else View.GONE
    }

    override fun getItemCount(): Int = artists.size

    fun updateData(newArtists: List<Artist>) {
        artists = newArtists
        notifyDataSetChanged()
    }
}
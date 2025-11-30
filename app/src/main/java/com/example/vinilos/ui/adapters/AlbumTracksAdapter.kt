package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.R
import com.example.vinilos.models.Track

/**
 * Adaptador para mostrar la lista de tracks en el detalle del álbum
 */
class AlbumTracksAdapter : RecyclerView.Adapter<AlbumTracksAdapter.TrackViewHolder>() {

    private var tracks = listOf<Track>()

    fun updateTracks(newTracks: List<Track>) {
        tracks = newTracks
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_track, parent, false)
        return TrackViewHolder(view)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track, position + 1)
    }

    override fun getItemCount(): Int = tracks.size

    class TrackViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val trackNumber: TextView = itemView.findViewById(R.id.trackNumber)
        private val trackName: TextView = itemView.findViewById(R.id.trackName)
        private val trackDuration: TextView = itemView.findViewById(R.id.trackDuration)

        fun bind(track: Track, position: Int) {
            trackNumber.text = position.toString()
            trackName.text = track.name
            trackDuration.text = track.duration
        }
    }
}


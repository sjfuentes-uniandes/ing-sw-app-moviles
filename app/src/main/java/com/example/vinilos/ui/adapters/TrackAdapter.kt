package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.vinilos.databinding.ListItemTrackBinding

data class Track(
    val id: String,
    val name: String
)

class TrackAdapter(private val onDeleteClick: (Track) -> Unit): ListAdapter<Track, TrackAdapter.TrackViewHolder>(TrackDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        val binding = ListItemTrackBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return TrackViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = getItem(position)
        holder.bind(track, onDeleteClick)
    }

    // ViewHolder que "sostiene" la vista de cada item
    class TrackViewHolder(private val binding: ListItemTrackBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(track: Track, onDeleteClick: (Track) -> Unit) {
            binding.trackNameTextView.text = track.name
            binding.deleteTrackButton.setOnClickListener {
                onDeleteClick(track)
            }
        }
    }

    // DiffUtil para animaciones y actualizaciones eficientes
    class TrackDiffCallback : DiffUtil.ItemCallback<Track>() {
        override fun areItemsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Track, newItem: Track): Boolean {
            return oldItem == newItem
        }
    }
}


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
import com.example.vinilos.databinding.CollectorItemBinding
import com.example.vinilos.models.Collector

class CollectorsAdapter: RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>() {
    var collectors : List<Collector> = emptyList()
        set(value){
            val diffCallback = CollectorDiffCallback(field, value)
            val diffResult = DiffUtil.calculateDiff(diffCallback)
            field = value
            try {
                diffResult.dispatchUpdatesTo(this)
            } catch (e: Exception) {
                // Skip notifications in unit tests
            }
        }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CollectorsAdapter.CollectorViewHolder {
        val binding: CollectorItemBinding = DataBindingUtil.inflate(
            LayoutInflater.from(parent.context),
            R.layout.collector_item,
            parent,
            false
        )
        return CollectorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CollectorViewHolder, position: Int) {
        holder.viewDataBinding.also {
            it.collector = collectors[position]
        }
        holder.bind(collectors[position])
    }

    override fun getItemCount(): Int = collectors.size

    class CollectorViewHolder(val viewDataBinding: CollectorItemBinding): RecyclerView.ViewHolder(viewDataBinding.root){
        fun bind(collector: Collector){
            Glide.with(itemView)
                .load("".toUri().buildUpon().scheme("https").build())
                .apply(
                    RequestOptions()
                        .placeholder(R.drawable.placeholder_image)
                        .error(R.drawable.placeholder_image)
                )
                .into(viewDataBinding.collectorImage)
        }
    }

    private class CollectorDiffCallback(private val oldList: List<Collector>, private val newList: List<Collector>) : DiffUtil.Callback() {
        override fun getOldListSize() = oldList.size
        override fun getNewListSize() = newList.size
        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) = 
            oldList[oldItemPosition].collectorId == newList[newItemPosition].collectorId
        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) = 
            oldList[oldItemPosition] == newList[newItemPosition]
    }
}

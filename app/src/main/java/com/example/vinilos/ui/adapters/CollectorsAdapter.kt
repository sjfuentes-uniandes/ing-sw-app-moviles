package com.example.vinilos.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.vinilos.R
import com.example.vinilos.databinding.CollectorItemBinding
import com.example.vinilos.models.Collector

class CollectorsAdapter: RecyclerView.Adapter<CollectorsAdapter.CollectorViewHolder>() {
    var collectors : List<Collector> = emptyList()
        set(value){
            field = value
            notifyDataSetChanged()
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
        holder.bind(collectors[position])
    }

    override fun getItemCount(): Int = collectors.size

    class CollectorViewHolder(val binding: CollectorItemBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(collector: Collector){
            binding.collector = collector

            Glide.with(binding.root.context)
                .load("")
                .placeholder(R.drawable.placeholder_image)
                .error(R.drawable.placeholder_image)
                .into(binding.collectorImage)

            binding.executePendingBindings()
        }
    }
}
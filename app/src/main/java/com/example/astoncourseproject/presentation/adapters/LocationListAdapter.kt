package com.example.astoncourseproject.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.astoncourseproject.databinding.LocationsListItemBinding
import com.example.astoncourseproject.presentation.models.LocationItem

class LocationListAdapter(
    private var onItemClick: (LocationItem) -> Unit,
) : ListAdapter<LocationItem, LocationListAdapter.LocationListAdapterViewHolder>(DiffCallback) {
    class LocationListAdapterViewHolder(
        private val binding: LocationsListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(locationItem: LocationItem) {
            with(binding) {
                locationName.text = locationItem.name
                locationDimen.text = locationItem.dimension
                locationType.text = locationItem.type
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): LocationListAdapterViewHolder {
        return LocationListAdapterViewHolder(
            LocationsListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: LocationListAdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
        holder.bind(currentItem)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<LocationItem>() {
            override fun areItemsTheSame(oldItem: LocationItem, newItem: LocationItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: LocationItem, newItem: LocationItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
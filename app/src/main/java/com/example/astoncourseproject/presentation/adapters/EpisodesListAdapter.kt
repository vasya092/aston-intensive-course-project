package com.example.astoncourseproject.presentation.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.astoncourseproject.databinding.EpisodesListItemBinding
import com.example.astoncourseproject.presentation.models.EpisodeItem

class EpisodesListAdapter(
    private var onItemClick: (EpisodeItem) -> Unit,
) : ListAdapter<EpisodeItem, EpisodesListAdapter.EpisodesListAdapterViewHolder>(DiffCallback) {
    class EpisodesListAdapterViewHolder(
        private val binding: EpisodesListItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(episodeItem: EpisodeItem) {
            with(binding) {
                episodeName.text = episodeItem.name
                episodeTitle.text = episodeItem.episode
                episodeDate.text = episodeItem.airDate
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EpisodesListAdapterViewHolder {
        return EpisodesListAdapterViewHolder(
            EpisodesListItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: EpisodesListAdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClick(currentItem)
        }
        holder.bind(currentItem)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<EpisodeItem>() {
            override fun areItemsTheSame(oldItem: EpisodeItem, newItem: EpisodeItem): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: EpisodeItem, newItem: EpisodeItem): Boolean {
                return oldItem == newItem
            }
        }
    }
}
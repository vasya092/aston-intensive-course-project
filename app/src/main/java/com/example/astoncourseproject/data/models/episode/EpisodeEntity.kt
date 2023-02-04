package com.example.astoncourseproject.data.models.episode

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "episodes")
data class EpisodeEntity (
    @PrimaryKey
    val id: Long,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<String>
)


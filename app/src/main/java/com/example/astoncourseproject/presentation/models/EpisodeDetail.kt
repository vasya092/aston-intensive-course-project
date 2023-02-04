package com.example.astoncourseproject.presentation.models

data class EpisodeDetail(
    val id: Long,
    val name: String,
    val airDate: String,
    val episode: String,
    val characters: List<String>
)
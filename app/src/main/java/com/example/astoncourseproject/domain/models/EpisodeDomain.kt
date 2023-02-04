package com.example.astoncourseproject.domain.models

import com.google.gson.annotations.SerializedName

data class EpisodeDomain(
    val id: Long,
    val name: String,
    @SerializedName("air_date")
    val airDate: String,
    val episode: String,
    val characters: List<String>
)

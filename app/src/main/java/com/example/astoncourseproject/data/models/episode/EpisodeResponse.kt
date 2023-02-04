package com.example.astoncourseproject.data.models.episode

import com.example.astoncourseproject.data.models.QueryInfo
import com.example.astoncourseproject.domain.models.EpisodeDomain

data class EpisodeResponse(
    val info: QueryInfo,
    var results: List<EpisodeDomain>
)


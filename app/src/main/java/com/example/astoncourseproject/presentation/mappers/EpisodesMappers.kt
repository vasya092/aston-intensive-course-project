package com.example.astoncourseproject.presentation.mappers

import com.example.astoncourseproject.domain.models.EpisodeDomain
import com.example.astoncourseproject.presentation.models.EpisodeDetail
import com.example.astoncourseproject.presentation.models.EpisodeItem

fun List<EpisodeDomain>.asEpisodeItems(): List<EpisodeItem> {
    return map {
        EpisodeItem(it.id, it.name, it.airDate,it.episode)
    }
}

fun EpisodeDomain.asEpisodeDetail(): EpisodeDetail {
    return EpisodeDetail(this.id, this.name, this.airDate,this.episode,this.characters)
}
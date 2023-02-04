package com.example.astoncourseproject.data.mappers

import com.example.astoncourseproject.data.models.episode.EpisodeEntity
import com.example.astoncourseproject.domain.models.EpisodeDomain

fun List<EpisodeDomain>.asEpisodeEntity(): List<EpisodeEntity> {
    return map {
        EpisodeEntity(it.id,it.name,it.airDate,it.episode, it.characters)
    }
}

fun List<EpisodeEntity>.asEpisodeDomain(): List<EpisodeDomain> {
    return map {
        EpisodeDomain(it.id,it.name,it.airDate,it.episode, it.characters)
    }
}
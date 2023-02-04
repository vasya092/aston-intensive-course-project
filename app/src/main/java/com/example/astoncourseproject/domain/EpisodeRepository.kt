package com.example.astoncourseproject.domain

import com.example.astoncourseproject.data.models.episode.EpisodeResponse
import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.domain.models.EpisodeDomain
import kotlinx.coroutines.flow.Flow

interface EpisodeRepository {

    val episodes: Flow<List<EpisodeDomain>>

    suspend fun getById(id: Int): Flow<EpisodeDomain?>

    suspend fun refreshDatabase(): EpisodeResponse

    suspend fun loadNewPage(queryLink: String): EpisodeResponse

    suspend fun findByName(name: String, page: Int? = 1): EpisodeResponse?

    suspend fun findWithFilter(name: String = "", episode: String, page: Int? = 1): EpisodeResponse?

    suspend fun findSomethingCharactersById(list: List<Int>): Flow<List<CharacterDomain>?>
}
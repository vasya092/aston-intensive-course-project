package com.example.astoncourseproject.domain

import com.example.astoncourseproject.data.models.character.CharacterResponse
import com.example.astoncourseproject.data.models.episode.EpisodeResponse
import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.domain.models.EpisodeDomain
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {

    var characters: Flow<List<CharacterDomain>>

    suspend fun getById(id: Int): Flow<CharacterDomain?>

    suspend fun refreshDatabase(): CharacterResponse

    suspend fun loadNewPage(queryLink: String): CharacterResponse

    suspend fun findByName(characterName: String, page: Int? = 1): CharacterResponse?

    suspend fun findWithFilter(name: String = "", status: String, species: String, gender: String, type: String, page: Int? = 1): CharacterResponse?

    suspend fun findSomethingEpisodesById(list: List<Int>): Flow<List<EpisodeDomain>?>
}

package com.example.astoncourseproject.data.repositories

import com.example.astoncourseproject.data.local.LocalDatabaseDao
import com.example.astoncourseproject.data.mappers.asEpisodeDomain
import com.example.astoncourseproject.data.mappers.asEpisodeEntity
import com.example.astoncourseproject.data.models.QueryInfo
import com.example.astoncourseproject.data.models.episode.EpisodeResponse
import com.example.astoncourseproject.data.network.MortyService
import com.example.astoncourseproject.domain.EpisodeRepository
import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.domain.models.EpisodeDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class EpisodeRepositoryImpl(
    private val mortyService: MortyService,
    private val localDatabaseDao: LocalDatabaseDao,
) : EpisodeRepository {
    override val episodes: Flow<List<EpisodeDomain>> = localDatabaseDao.queryEpisodes().map {
        it.asEpisodeDomain()
    }

    override suspend fun getById(id: Int): Flow<EpisodeDomain?> {
        var item: EpisodeDomain? = null
        kotlin.runCatching {
            item = mortyService.getEpisodeDetailById(id)
        }.onFailure {
            withContext(Dispatchers.IO) {
                item = localDatabaseDao.queryEpisodeById(id)
            }
        }

        return flow {
            emit(item)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun refreshDatabase(): EpisodeResponse {
        val response = mortyService.getEpisodes(1)
        localDatabaseDao.insertEpisodes(response.results.asEpisodeEntity())
        return response
    }

    override suspend fun loadNewPage(queryLink: String): EpisodeResponse {
        val response = mortyService.getEpisodeNextPage(queryLink)
        localDatabaseDao.insertEpisodes(response.results.asEpisodeEntity())
        return response
    }

    override suspend fun findByName(name: String, page: Int?): EpisodeResponse? {
        var response: EpisodeResponse? = null
        kotlin.runCatching {
            response = mortyService.getEpisodeByName(name, page)
        }.onFailure {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    localDatabaseDao.queryEpisodesByName(name)
                }.onSuccess {
                    if (it.isNotEmpty()) {
                        response = EpisodeResponse(
                            info = QueryInfo(0, 0, ""),
                            results = it
                        )
                    }
                }.onFailure {
                    response = null
                }
            }
        }
        return response
    }

    override suspend fun findWithFilter(
        name: String,
        episode: String,
        page: Int?,
    ): EpisodeResponse? {
        var response: EpisodeResponse? = null
        kotlin.runCatching {
            response = mortyService.getEpisodeWithFilters(name, episode, page)
        }.onFailure {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    localDatabaseDao.queryEpisodesWithFilter(name, episode)
                }.onSuccess {
                    if (it.isNotEmpty()) {
                        response = EpisodeResponse(
                            info = QueryInfo(0, 0, ""),
                            results = it.asEpisodeDomain()
                        )
                    }
                }.onFailure {
                    response = null
                }

            }
        }
        return response
    }

    override suspend fun findSomethingCharactersById(list: List<Int>): Flow<List<CharacterDomain>?> {
        var charactersList: List<CharacterDomain>? = null
        kotlin.runCatching {
            charactersList = mortyService.getSomethingCharactersById(list)
        }.onFailure {
            withContext(Dispatchers.IO) {
                charactersList = localDatabaseDao.queryCharacterBySomethingId(list)
            }
        }

        return flow {
            emit(charactersList)
        }.flowOn(Dispatchers.IO)
    }
}
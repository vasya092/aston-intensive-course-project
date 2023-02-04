package com.example.astoncourseproject.data.repositories

import android.util.Log
import com.example.astoncourseproject.data.models.character.CharacterResponse
import com.example.astoncourseproject.data.local.LocalDatabaseDao
import com.example.astoncourseproject.data.mappers.asCharacterDomain
import com.example.astoncourseproject.data.mappers.asCharacterEntity
import com.example.astoncourseproject.data.models.QueryInfo
import com.example.astoncourseproject.data.network.MortyService
import com.example.astoncourseproject.domain.CharacterRepository
import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.domain.models.EpisodeDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CharacterRepositoryImpl @Inject constructor(
    private val mortyService: MortyService,
    private val localDatabaseDao: LocalDatabaseDao,
) : CharacterRepository {

    override var characters: Flow<List<CharacterDomain>> = localDatabaseDao.queryCharacters().map {
        it.asCharacterDomain()
    }

    override suspend fun getById(id: Int): Flow<CharacterDomain?> {
        var item: CharacterDomain? = null
        kotlin.runCatching {
            item = mortyService.getCharacterDetailById(id)
        }.onFailure {
            withContext(Dispatchers.IO) {
                item = localDatabaseDao.queryCharacterById(id)
            }
        }

        return flow {
            emit(item)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun refreshDatabase(): CharacterResponse {
        val response = mortyService.getCharacters(1)
        localDatabaseDao.insertCharacter(response.results.asCharacterEntity())
        return response
    }

    override suspend fun loadNewPage(queryLink: String): CharacterResponse {
        val response = mortyService.getCharacterNextPage(queryLink)
        localDatabaseDao.insertCharacter(response.results.asCharacterEntity())
        return response
    }

    override suspend fun findByName(characterName: String, page: Int?): CharacterResponse? {
        var response: CharacterResponse? = null
        kotlin.runCatching {
            response = mortyService.getCharactersByName(characterName, page)
        }.onFailure {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    localDatabaseDao.queryCharactersByName(characterName)
                }.onSuccess {
                    if (it.isNotEmpty()) {
                        response = CharacterResponse(
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
        status: String,
        species: String,
        gender: String,
        type: String,
        page: Int?,
    ): CharacterResponse? {
        var response: CharacterResponse? = null
        kotlin.runCatching {
            response =
                mortyService.getCharactersWithFilters(name, status, species, gender, type, page)
        }.onFailure {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    localDatabaseDao.queryCharactersWithFilter(name, status, species, gender, type)
                }.onSuccess {
                    if (it.isNotEmpty()) {
                        response = CharacterResponse(
                            info = QueryInfo(0, 0, ""),
                            results = it.asCharacterDomain()
                        )
                    }
                }.onFailure {
                    response = null
                }

            }
        }
        return response
    }

    override suspend fun findSomethingEpisodesById(list: List<Int>): Flow<List<EpisodeDomain>?> {
        var episodesList: List<EpisodeDomain>? = null
        kotlin.runCatching {
            episodesList = mortyService.getSomethingEpisodesById(list)
        }.onFailure {
            withContext(Dispatchers.IO) {
                kotlin.runCatching { localDatabaseDao.queryEpisodesBySomethingId(list) }
                    .onSuccess { episodesList = it }
                    .onFailure { Log.e("TAG", it.message.toString()) }

            }
        }

        return flow {
            emit(episodesList)
        }.flowOn(Dispatchers.IO)
    }
}


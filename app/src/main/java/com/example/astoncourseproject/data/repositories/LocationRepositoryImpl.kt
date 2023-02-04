package com.example.astoncourseproject.data.repositories

import android.util.Log
import com.example.astoncourseproject.data.models.location.LocationResponse
import com.example.astoncourseproject.data.local.LocalDatabaseDao
import com.example.astoncourseproject.data.mappers.asLocationEntity
import com.example.astoncourseproject.data.mappers.asLocationsDomain
import com.example.astoncourseproject.data.models.QueryInfo
import com.example.astoncourseproject.data.network.MortyService
import com.example.astoncourseproject.domain.LocationRepository
import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.domain.models.LocationDomain
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class LocationRepositoryImpl(
    private val mortyService: MortyService,
    private val localDatabaseDao: LocalDatabaseDao,
) : LocationRepository {

    override val locations: Flow<List<LocationDomain>> = localDatabaseDao.queryLocations().map {
        it.asLocationsDomain()
    }

    override suspend fun getById(id: Int): Flow<LocationDomain?> {
        var item: LocationDomain? = null
        kotlin.runCatching {
            item = mortyService.getLocationDetailById(id)
        }.onFailure {
            withContext(Dispatchers.IO) {
                item = localDatabaseDao.queryLocationById(id)
            }
        }

        return flow {
            emit(item)
        }.flowOn(Dispatchers.IO)
    }

    override suspend fun refreshDatabase(): LocationResponse {
        val response = mortyService.getLocations(1)
        localDatabaseDao.insertLocation(response.results.asLocationEntity())
        return response
    }

    override suspend fun loadNewPage(queryLink: String): LocationResponse {
        val response = mortyService.getLocationNextPage(queryLink)
        localDatabaseDao.insertLocation(response.results.asLocationEntity())
        return response
    }

    override suspend fun findByName(name: String, page: Int?): LocationResponse? {
        var response: LocationResponse? = null
        kotlin.runCatching {
            response = mortyService.getLocationByName(name, page)
        }.onFailure {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    localDatabaseDao.queryLocationsByName(name)
                }.onSuccess {
                    if (it.isNotEmpty()) {
                        response = LocationResponse(
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
        type: String,
        dimension: String,
        page: Int?,
    ): LocationResponse? {
        var response: LocationResponse? = null
        kotlin.runCatching {
            response = mortyService.getLocationsWithFilters(name, type, dimension, page)
        }.onFailure {
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    localDatabaseDao.queryLocationsWithFilter(name, type, dimension)
                }.onSuccess {
                    if (it.isNotEmpty()) {
                        response = LocationResponse(
                            info = QueryInfo(0, 0, ""),
                            results = it.asLocationsDomain()
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
                kotlin.runCatching { localDatabaseDao.queryCharacterBySomethingId(list) }
                    .onSuccess { charactersList = it }
                    .onFailure { Log.e("TAG", it.message.toString()) }

            }
        }

        return flow {
            emit(charactersList)
        }.flowOn(Dispatchers.IO)
    }
}
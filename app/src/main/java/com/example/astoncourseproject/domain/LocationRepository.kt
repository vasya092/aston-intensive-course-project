package com.example.astoncourseproject.domain

import com.example.astoncourseproject.data.models.location.LocationResponse
import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.domain.models.LocationDomain
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    val locations: Flow<List<LocationDomain>>

    suspend fun getById(id: Int): Flow<LocationDomain?>

    suspend fun refreshDatabase(): LocationResponse

    suspend fun loadNewPage(queryLink: String): LocationResponse

    suspend fun findByName(name: String, page: Int? = 1): LocationResponse?

    suspend fun findWithFilter(name: String = "",type: String,dimension: String, page: Int? = 1): LocationResponse?

    suspend fun findSomethingCharactersById(list: List<Int>): Flow<List<CharacterDomain>?>
}
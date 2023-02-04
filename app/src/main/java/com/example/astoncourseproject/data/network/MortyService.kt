package com.example.astoncourseproject.data.network

import com.example.astoncourseproject.data.models.character.CharacterResponse
import com.example.astoncourseproject.data.models.episode.EpisodeResponse
import com.example.astoncourseproject.data.models.location.LocationResponse
import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.domain.models.EpisodeDomain
import com.example.astoncourseproject.domain.models.LocationDomain
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface MortyService {

    @GET("character/{id}")
    suspend fun getCharacterDetailById(
        @Path("id") id: Int
    ): CharacterDomain

    @GET("character/{id}")
    suspend fun getSomethingCharactersById(
        @Path("id") id: List<Int>
    ): List<CharacterDomain>

    @GET("character")
    suspend fun getCharacters(@Query("page") pageNumber: Int): CharacterResponse

    @GET("character")
    suspend fun getCharactersByName(@Query("name") characterName: String, @Query("page") page: Int?): CharacterResponse

    @GET("character?")
    suspend fun getCharacterNextPage(
        @Query("page") nextPage:String
    ): CharacterResponse

    @GET("character")
    suspend fun getCharactersWithFilters(
        @Query("name") name: String,
        @Query("status") status: String,
        @Query("species") species: String,
        @Query("gender") gender: String,
        @Query("type") type: String,
        @Query("page") page: Int?
    ): CharacterResponse

    @GET("location")
    suspend fun getLocations(@Query("page") pageNumber: Int): LocationResponse

    @GET("location/{id}")
    suspend fun getLocationDetailById(
        @Path("id") id: Int
    ): LocationDomain

    @GET("location?")
    suspend fun getLocationNextPage(
        @Query("page") nextPage:String
    ): LocationResponse

    @GET("location")
    suspend fun getLocationsWithFilters(
        @Query("name") name: String,
        @Query("type") type: String,
        @Query("dimension") dimension: String,
        @Query("page") page: Int?
    ): LocationResponse

    @GET("location")
    suspend fun getLocationByName(@Query("name") locationName: String, @Query("page") page: Int?): LocationResponse

    @GET("episode")
    suspend fun getEpisodes(@Query("page") pageNumber: Int): EpisodeResponse

    @GET("episode/{id}")
    suspend fun getEpisodeDetailById(
        @Path("id") id: Int
    ): EpisodeDomain

    @GET("episode?")
    suspend fun getEpisodeNextPage(
        @Query("page") nextPage:String
    ): EpisodeResponse

    @GET("episode")
    suspend fun getEpisodeWithFilters(
        @Query("name") name: String,
        @Query("episode") episode: String,
        @Query("page") page: Int?
    ): EpisodeResponse

    @GET("episode")
    suspend fun getEpisodeByName(@Query("name") name: String, @Query("page") page: Int?): EpisodeResponse

    @GET("episode/{id}")
    suspend fun getSomethingEpisodesById(
        @Path("id") id: List<Int>
    ): List<EpisodeDomain>
}
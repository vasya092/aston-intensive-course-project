package com.example.astoncourseproject.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.astoncourseproject.data.models.character.CharacterEntity
import com.example.astoncourseproject.data.models.episode.EpisodeEntity
import com.example.astoncourseproject.data.models.location.LocationEntity
import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.domain.models.EpisodeDomain
import com.example.astoncourseproject.domain.models.LocationDomain
import kotlinx.coroutines.flow.Flow

@Dao
interface LocalDatabaseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertCharacter(data: List<CharacterEntity>)

    @Query("SELECT * from characters")
    fun queryCharacters(): Flow<List<CharacterEntity>>

    @Query("SELECT * from characters WHERE id = :userId")
    fun queryCharacterById(userId: Int): CharacterDomain

    @Query("SELECT * from characters WHERE id IN (:userId)")
    fun queryCharacterBySomethingId(userId: List<Int>): List<CharacterDomain>

    @Query("SELECT * FROM characters WHERE (:name IS NULL OR name LIKE '%'||:name||'%')" +
            "AND (:status IS NULL OR status LIKE '%'||:status||'%')" +
            "AND (:species IS NULL OR species LIKE '%'||:species||'%')" +
            "AND (:gender IS NULL OR gender LIKE '%'||:gender||'%')" +
            "AND (:type IS NULL OR type LIKE '%'||:type||'%')")
    fun queryCharactersWithFilter(name: String,status: String, species: String, gender: String, type: String): List<CharacterEntity>

    @Query("SELECT * FROM characters WHERE (:name IS NULL OR name LIKE '%'||:name||'%')")
    fun queryCharactersByName(name: String): List<CharacterDomain>

    @Query("SELECT * FROM locations WHERE (:name IS NULL OR name LIKE '%'||:name||'%')")
    fun queryLocationsByName(name: String): List<LocationDomain>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLocation(data: List<LocationEntity>)

    @Query("SELECT * from locations")
    fun queryLocations(): Flow<List<LocationEntity>>

    @Query("SELECT * from locations WHERE id = :locationId")
    fun queryLocationById(locationId: Int): LocationDomain

    @Query("SELECT * FROM locations WHERE (:name IS NULL OR name LIKE '%'||:name||'%')" +
            "AND (:type IS NULL OR type LIKE '%'||:type||'%')" +
            "AND (:dimension IS NULL OR dimension LIKE '%'||:dimension||'%')")
    fun queryLocationsWithFilter(name: String, type: String, dimension: String): List<LocationEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertEpisodes(data: List<EpisodeEntity>)

    @Query("SELECT * from episodes")
    fun queryEpisodes(): Flow<List<EpisodeEntity>>

    @Query("SELECT * from episodes WHERE id = :episodeId")
    fun queryEpisodeById(episodeId: Int): EpisodeDomain

    @Query("SELECT * FROM episodes WHERE (:name IS NULL OR name LIKE '%'||:name||'%')")
    fun queryEpisodesByName(name: String): List<EpisodeDomain>

    @Query("SELECT * from episodes WHERE id IN (:episodeId)")
    fun queryEpisodesBySomethingId(episodeId: List<Int>): List<EpisodeDomain>

    @Query("SELECT * FROM episodes WHERE (:name IS NULL OR name LIKE '%'||:name||'%')" +
            "AND (:episode IS NULL OR episode LIKE '%'||:episode||'%')")
    fun queryEpisodesWithFilter(name: String, episode: String): List<EpisodeEntity>
}
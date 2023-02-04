package com.example.astoncourseproject.data.models.character

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.astoncourseproject.domain.models.LocationLinkDomain

@Entity(tableName = "characters")
data class CharacterEntity (
    @PrimaryKey
    val id: Long,
    val name: String,
    val status: String,
    val species: String,
    val gender: String,
    val type: String,
    val image: String,
    val origin: LocationLinkDomain,
    val location: LocationLinkDomain,
    val episode: List<String>
)
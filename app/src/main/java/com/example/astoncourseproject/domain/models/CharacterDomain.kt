package com.example.astoncourseproject.domain.models

data class CharacterDomain(
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

data class LocationLinkDomain (
    val name: String,
    val url: String,
)
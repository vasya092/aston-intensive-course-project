package com.example.astoncourseproject.presentation.models

import com.example.astoncourseproject.domain.models.LocationLinkDomain

data class CharacterDetail(
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
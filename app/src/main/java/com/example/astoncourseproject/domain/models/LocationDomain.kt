package com.example.astoncourseproject.domain.models

data class LocationDomain(
    val id: Long,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
)
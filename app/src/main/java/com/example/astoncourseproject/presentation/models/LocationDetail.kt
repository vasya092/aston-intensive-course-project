package com.example.astoncourseproject.presentation.models

data class LocationDetail(
    val id: Long,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
)
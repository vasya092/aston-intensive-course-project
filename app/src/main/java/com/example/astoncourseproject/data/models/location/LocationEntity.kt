package com.example.astoncourseproject.data.models.location

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity (
    @PrimaryKey
    val id: Long,
    val name: String,
    val type: String,
    val dimension: String,
    val residents: List<String>
)


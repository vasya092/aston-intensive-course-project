package com.example.astoncourseproject.data.models.location

import com.example.astoncourseproject.data.models.QueryInfo
import com.example.astoncourseproject.domain.models.LocationDomain

data class LocationResponse(
    val info: QueryInfo,
    var results: List<LocationDomain>
)


package com.example.astoncourseproject.data.mappers

import com.example.astoncourseproject.data.models.location.LocationEntity
import com.example.astoncourseproject.domain.models.LocationDomain

fun List<LocationDomain>.asLocationEntity(): List<LocationEntity> {
    return map {
        LocationEntity(it.id, it.name,it.type,it.dimension, it.residents)
    }
}

fun List<LocationEntity>.asLocationsDomain(): List<LocationDomain> {
    return map {
        LocationDomain(it.id, it.name,it.type,it.dimension, it.residents)
    }
}
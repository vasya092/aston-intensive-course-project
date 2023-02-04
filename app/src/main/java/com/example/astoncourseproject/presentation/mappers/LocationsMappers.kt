package com.example.astoncourseproject.presentation.mappers

import com.example.astoncourseproject.domain.models.LocationDomain
import com.example.astoncourseproject.presentation.models.LocationDetail
import com.example.astoncourseproject.presentation.models.LocationItem

fun List<LocationDomain>.asLocationItems(): List<LocationItem> {
    return map {
        LocationItem(it.id, it.name, it.type, it.dimension)
    }
}

fun LocationDomain.asLocationDetail(): LocationDetail {
    return LocationDetail(this.id, this.name, this.type, this.dimension, this.residents)
}
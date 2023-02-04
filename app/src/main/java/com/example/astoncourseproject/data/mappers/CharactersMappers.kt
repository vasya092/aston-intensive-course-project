package com.example.astoncourseproject.data.mappers

import com.example.astoncourseproject.data.models.character.CharacterEntity
import com.example.astoncourseproject.domain.models.CharacterDomain

fun List<CharacterDomain>.asCharacterEntity(): List<CharacterEntity> {
    return map {
        CharacterEntity(it.id, it.name,it.status,it.species, it.gender, it.type, it.image, it.origin,it.location,it.episode)
    }
}

fun List<CharacterEntity>.asCharacterDomain(): List<CharacterDomain> {
    return map {
        CharacterDomain(it.id, it.name,it.status,it.species, it.gender, it.type, it.image, it.origin,it.location, it.episode)
    }
}
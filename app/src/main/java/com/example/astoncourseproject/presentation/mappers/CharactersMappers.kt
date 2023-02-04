package com.example.astoncourseproject.presentation.mappers

import com.example.astoncourseproject.domain.models.CharacterDomain
import com.example.astoncourseproject.presentation.models.Character
import com.example.astoncourseproject.presentation.models.CharacterDetail

fun List<CharacterDomain>.asCharacterItemsModel(): List<Character> {
    return map {
        Character(it.id, it.name, it.status, it.species, it.gender, it.type, it.image)
    }
}

fun CharacterDomain.asCharacterDetailModel(): CharacterDetail {
    return CharacterDetail(this.id,
        this.name,
        this.status,
        this.species,
        this.gender,
        this.type,
        this.image,
        this.origin,
        this.location,
        this.episode)
}
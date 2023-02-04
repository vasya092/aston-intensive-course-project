package com.example.astoncourseproject.data.models.character

import com.example.astoncourseproject.data.models.QueryInfo
import com.example.astoncourseproject.domain.models.CharacterDomain

data class CharacterResponse(
    val info: QueryInfo,
    val results: List<CharacterDomain>
)


package com.example.astoncourseproject.presentation.viewmodel

import androidx.lifecycle.*
import com.example.astoncourseproject.domain.CharacterRepository
import com.example.astoncourseproject.domain.models.LocationLinkDomain
import com.example.astoncourseproject.presentation.base.BaseDetailViewModel
import com.example.astoncourseproject.presentation.mappers.asCharacterDetailModel
import com.example.astoncourseproject.presentation.mappers.asEpisodeItems
import com.example.astoncourseproject.presentation.models.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CharacterDetailViewModel @AssistedInject constructor(
    id: Int,
    private val repository: CharacterRepository,
) : BaseDetailViewModel() {

    private var _character: MutableStateFlow<CharacterDetail> = MutableStateFlow(
        CharacterDetail(0,
            "",
            "",
            "",
            "",
            "",
            "",
            LocationLinkDomain("", ""),
            LocationLinkDomain("", ""),
            listOf())
    )
    val character: StateFlow<CharacterDetail> get() = _character

    private var _episodes: MutableStateFlow<List<EpisodeItem>> = MutableStateFlow(listOf())
    val episodes: StateFlow<List<EpisodeItem>> get() = _episodes

    init {
        getCharacter(id)
    }

    private fun getCharacter(id: Int) {
        updateStatus(Loading)
        viewModelScope.launch {
            repository.getById(id)
                .catch {
                    updateStatus(Failure)
                }.collect { characters ->
                    if (characters != null) {
                        updateStatus(Success)
                        _character.value = characters.asCharacterDetailModel()
                        val episodesId = getIdListFromSomethingUrl(characters.episode, "episode")
                        loadUserEpisodes(episodesId)
                    } else {
                        updateStatus(Failure)
                    }
                }
        }
    }

    private suspend fun loadUserEpisodes(episodesId: List<Int>) {
        updateStatus(Loading)
        repository.findSomethingEpisodesById(episodesId).catch {
            updateStatus(Failure)
        }.collect { episodes ->
            if (episodes != null) {
                _episodes.value = episodes.asEpisodeItems()
                updateStatus(Success)
            } else {
                updateStatus(Failure)
            }
        }
    }

}

class CharacterDetailViewModelFactory @AssistedInject constructor(
    @Assisted("characterId") private val characterId: Int,
    private val repository: CharacterRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return CharacterDetailViewModel(characterId, repository) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted("characterId") characterId: Int): CharacterDetailViewModelFactory
    }
}
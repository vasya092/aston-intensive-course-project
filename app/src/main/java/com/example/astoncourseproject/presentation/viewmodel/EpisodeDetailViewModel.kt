package com.example.astoncourseproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.astoncourseproject.domain.EpisodeRepository
import com.example.astoncourseproject.presentation.base.BaseDetailViewModel
import com.example.astoncourseproject.presentation.mappers.asCharacterItemsModel
import com.example.astoncourseproject.presentation.mappers.asEpisodeDetail
import com.example.astoncourseproject.presentation.models.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class EpisodeDetailViewModel @AssistedInject constructor(
    id: Int,
    private val repository: EpisodeRepository,
) : BaseDetailViewModel() {

    private var _episode: MutableStateFlow<EpisodeDetail> = MutableStateFlow(
        EpisodeDetail(0,
            "",
            "",
            "",
            listOf())
    )
    val episode: StateFlow<EpisodeDetail> get() = _episode

    private var _characters: MutableStateFlow<List<Character>> = MutableStateFlow(listOf())
    val characters: StateFlow<List<Character>> get() = _characters

    init {
        getEpisode(id)
    }

    private fun getEpisode(id: Int) {
        updateStatus(Loading)
        viewModelScope.launch {
            repository.getById(id)
                .catch {
                    updateStatus(Failure)
                }.collect { episode ->
                    if (episode != null) {
                        _episode.value = episode.asEpisodeDetail()
                        updateStatus(Success)
                        val charactersId =
                            getIdListFromSomethingUrl(episode.characters, "character")
                        loadEpisodeCharacters(charactersId)
                    } else {
                        updateStatus(Failure)
                    }
                }
        }
    }

    private suspend fun loadEpisodeCharacters(charactersId: List<Int>) {
        updateStatus(Loading)
        repository.findSomethingCharactersById(charactersId).catch {
            updateStatus(Failure)
        }.collect {
            if (it != null) {
                _characters.value = it.asCharacterItemsModel()
                updateStatus(Success)
            } else {
                updateStatus(Failure)
            }
        }
    }
}

class EpisodeDetailViewModelFactory @AssistedInject constructor(
    @Assisted("episodeId") private val characterId: Int,
    private val repository: EpisodeRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return EpisodeDetailViewModel(characterId, repository) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted("episodeId") characterId: Int): EpisodeDetailViewModelFactory
    }
}
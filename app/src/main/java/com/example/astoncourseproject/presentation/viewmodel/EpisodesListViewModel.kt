package com.example.astoncourseproject.presentation.viewmodel

import androidx.lifecycle.*
import com.example.astoncourseproject.domain.EpisodeRepository
import com.example.astoncourseproject.presentation.base.BaseViewModel
import com.example.astoncourseproject.presentation.mappers.asEpisodeItems
import com.example.astoncourseproject.presentation.models.*
import com.example.astoncourseproject.utils.findParameterValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI
import javax.inject.Inject


class EpisodesListViewModel @Inject constructor(
    private val repository: EpisodeRepository,
) : BaseViewModel<EpisodeItem>() {

    val itemsFlow: StateFlow<List<EpisodeItem>> = repository.episodes.map { itemsList ->
        itemsList.asEpisodeItems()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    private val _searchField: MutableStateFlow<String> = MutableStateFlow("")
    val searchField: StateFlow<String> get() = _searchField

    private var _isFilterActive: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFilterActive: StateFlow<Boolean> get() = _isFilterActive

    private var _episodesFilter: FilterItem = FilterItem()
    val episodesFilter get() = _episodesFilter

    var nextPage: Int? = null

    init {
        refreshFromRepository()
    }

    private fun refreshFromRepository() = viewModelScope.launch {
        updateStatus(Loading)
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                kotlin.runCatching { repository.refreshDatabase() }
                    .onSuccess { updateStatus(Success) }
                    .onFailure { updateStatus(Failure) }
            }
        }
    }

    fun onFindByNameWithoutFilter(name: String, page: Int? = 1) {
        if (status.value != Loading) {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    updateStatus(Loading)
                    kotlin.runCatching { repository.findByName(name, page) }
                        .onSuccess {
                            if (it != null) {
                                updateNextPage(it.info.next)
                                if (name != "") {
                                    if (page == 1) {
                                        fillFilteredList(it.results.asEpisodeItems())
                                    } else {
                                        val modifiedList = filteredList.value.toMutableList()
                                        modifiedList.addAll(it.results.asEpisodeItems())
                                        fillFilteredList(modifiedList)
                                    }
                                    updateStatus(Success)
                                } else {
                                    fillFilteredList(listOf())
                                    updateStatus(Success)
                                }
                            } else {
                                updateStatus(CacheFailure)
                            }
                        }
                        .onFailure { updateStatus(Failure) }
                }
            }
        }
    }

    fun onUpdateSearchField(text: String) {
        _searchField.value = text
    }

    fun onClearSearchField() {
        _searchField.value = ""
    }

    fun onFindWithFilter(
        name: String = "",
        episodes: FilterItem,
        page: Int? = 1,
    ) {
        _isFilterActive.value = true
        _episodesFilter = episodes

        viewModelScope.launch {
            updateStatus(Loading)
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    repository.findWithFilter(name,
                        episodes.value,
                        page)
                }
                    .onSuccess { response ->
                        if (response != null) {
                            updateNextPage(response.info.next)
                            val updatedFilteredList = mutableListOf<EpisodeItem>()
                            updatedFilteredList.addAll(response.results.asEpisodeItems())
                            fillFilteredList(updatedFilteredList)
                            updateStatus(Success)
                        } else {
                            updateStatus(Failure)
                        }
                    }
                    .onFailure { updateStatus(Failure) }

            }
        }
    }

    private fun updateNextPage(newPage: String?) {
        nextPage = if (!newPage.isNullOrEmpty()) {
            val newPageUri = URI(newPage)
            val pageNum = newPageUri.findParameterValue("page")
            pageNum?.toInt()
        } else {
            null
        }
    }

    fun onListScrolled() {
        if (!isFilterActive.value && searchField.value == "") {
            viewModelScope.launch {
                withContext(Dispatchers.IO) {
                    kotlin.runCatching { repository.loadNewPage((itemsFlow.value.last().id / ITEMS_PER_PAGE + 1).toString()) }
                        .onSuccess { updateStatus(Success) }
                        .onFailure { updateStatus(Failure) }
                }
            }
        } else if (searchField.value != "" && !isFilterActive.value) {
            if (nextPage != null) {
                onFindByNameWithoutFilter(searchField.value, nextPage)
            }
        } else {
            if (nextPage != null) {
                if (status.value != Loading) {
                    updateStatus(Loading)
                    viewModelScope.launch {
                        withContext(Dispatchers.Default) {
                            kotlin.runCatching {
                                repository.findWithFilter(
                                    searchField.value,
                                    episodesFilter.value,
                                    nextPage
                                )
                            }
                            .onSuccess { response ->
                                if (response != null) {
                                    updateNextPage(response.info.next)
                                    val updatedFilteredList = getFilteredList().toMutableList()
                                    updatedFilteredList.addAll(response.results.asEpisodeItems())
                                    fillFilteredList(updatedFilteredList)
                                    updateStatus(Success)
                                }
                            }
                            .onFailure {
                                updateStatus(Failure)
                            }
                        }
                    }
                }
            }
        }
    }

    fun clearFilter() {
        _isFilterActive.value = false
        clearFilteredList()
        _searchField.value = ""
        _episodesFilter = FilterItem()
    }
}
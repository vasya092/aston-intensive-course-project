package com.example.astoncourseproject.presentation.viewmodel

import androidx.lifecycle.*
import com.example.astoncourseproject.domain.CharacterRepository
import com.example.astoncourseproject.presentation.base.BaseViewModel
import com.example.astoncourseproject.presentation.mappers.asCharacterItemsModel
import com.example.astoncourseproject.presentation.models.*
import com.example.astoncourseproject.utils.findParameterValue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URI
import javax.inject.Inject


class CharacterListViewModel @Inject constructor(
    private val repository: CharacterRepository,
) : BaseViewModel<Character>() {

    val itemsFlow: StateFlow<List<Character>> = repository.characters.map { itemsList ->
        itemsList.asCharacterItemsModel()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = listOf()
    )

    private val _searchField: MutableStateFlow<String> = MutableStateFlow("")
    val searchField: StateFlow<String> get() = _searchField

    private var _isFilterActive: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isFilterActive: StateFlow<Boolean> get() = _isFilterActive

    private var _selectedStatusFilter: FilterItem = FilterItem()
    val selectedStatusFilter get() = _selectedStatusFilter

    private var _selectedSpeciesFilter: FilterItem = FilterItem()
    val selectedSpeciesFilter get() = _selectedSpeciesFilter

    private var _selectedGenderFilter: FilterItem = FilterItem()
    val selectedGenderFilter get() = _selectedGenderFilter

    private var _typeFilter: FilterItem = FilterItem()
    val typeFilter get() = _typeFilter

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
                updateStatus(Loading)
                withContext(Dispatchers.IO) {
                    kotlin.runCatching { repository.findByName(name, page) }
                    .onSuccess {
                        if (it != null) {
                            updateNextPage(it.info.next)
                            if (name != "") {
                                if (page == 1) {
                                    fillFilteredList(it.results.asCharacterItemsModel())
                                } else {
                                    val modifiedList = filteredList.value.toMutableList()
                                    modifiedList.addAll(it.results.asCharacterItemsModel())
                                    fillFilteredList(modifiedList)
                                }
                                updateStatus(Success)
                            } else {
                                fillFilteredList(listOf())
                                updateStatus(Success)
                            }
                            updateStatus(Success)
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
        status: FilterItem,
        species: FilterItem,
        gender: FilterItem,
        type: FilterItem,
        page: Int? = 1,
    ) {

        _isFilterActive.value = true
        _selectedStatusFilter = status
        _selectedSpeciesFilter = species
        _selectedGenderFilter = gender
        _typeFilter = type

        viewModelScope.launch {
            updateStatus(Loading)
            withContext(Dispatchers.IO) {
                kotlin.runCatching {
                    repository.findWithFilter(name,
                        status.value,
                        species.value,
                        gender.value,
                        type.value,
                        page)
                }
                .onSuccess { response ->
                    if (response != null) {
                        updateNextPage(response.info.next)
                        val updatedFilteredList = mutableListOf<Character>()
                        updatedFilteredList.addAll(response.results.asCharacterItemsModel())
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
                                    selectedStatusFilter.value,
                                    selectedSpeciesFilter.value,
                                    selectedGenderFilter.value,
                                    typeFilter.value,
                                    nextPage
                                )
                            }
                                .onSuccess { response ->
                                    if (response != null) {
                                        updateNextPage(response.info.next)
                                        val updatedFilteredList = getFilteredList().toMutableList()
                                        updatedFilteredList.addAll(response.results.asCharacterItemsModel())
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
        _selectedStatusFilter = FilterItem()
        _selectedSpeciesFilter = FilterItem()
        _selectedGenderFilter = FilterItem()
    }

}
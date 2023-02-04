package com.example.astoncourseproject.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.astoncourseproject.domain.LocationRepository
import com.example.astoncourseproject.presentation.base.BaseDetailViewModel
import com.example.astoncourseproject.presentation.mappers.*
import com.example.astoncourseproject.presentation.models.*
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class LocationDetailViewModel @AssistedInject constructor(
    id: Int,
    private val repository: LocationRepository,
) : BaseDetailViewModel() {

    private var _location: MutableStateFlow<LocationDetail> = MutableStateFlow(
        LocationDetail(0,
            "",
            "",
            "",
            listOf())
    )
    val location: StateFlow<LocationDetail> get() = _location

    private var _characters: MutableStateFlow<List<Character>> = MutableStateFlow(listOf())
    val characters: StateFlow<List<Character>> get() = _characters

    init {
        getLocation(id)
    }

    private fun getLocation(id: Int) {
        updateStatus(Loading)
        viewModelScope.launch {
            repository.getById(id)
                .catch {
                    updateStatus(Failure)
                }.collect { location ->
                    if (location != null) {
                        _location.value = location.asLocationDetail()
                        updateStatus(Success)
                        val residentsId = getIdListFromSomethingUrl(location.residents, "character")
                        loadDetailResidents(residentsId)
                    } else {
                        updateStatus(Failure)
                    }
                }
        }
    }

    private suspend fun loadDetailResidents(residentsId: List<Int>) {
        updateStatus(Loading)
        repository.findSomethingCharactersById(residentsId).catch {
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

class LocationDetailViewModelFactory @AssistedInject constructor(
    @Assisted("locationId") private val locationId: Int,
    private val repository: LocationRepository,
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return LocationDetailViewModel(locationId, repository) as T
    }

    @AssistedFactory
    interface Factory {

        fun create(@Assisted("locationId") locationId: Int): LocationDetailViewModelFactory
    }
}
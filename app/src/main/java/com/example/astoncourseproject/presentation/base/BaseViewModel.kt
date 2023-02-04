package com.example.astoncourseproject.presentation.base

import androidx.lifecycle.ViewModel
import com.example.astoncourseproject.presentation.models.QueryStatus
import com.example.astoncourseproject.presentation.models.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseViewModel<T>() : ViewModel() {

    private val _filteredList: MutableStateFlow<List<T>> = MutableStateFlow(mutableListOf())
    val filteredList: StateFlow<List<T>> get() = _filteredList

    private val _status: MutableStateFlow<QueryStatus> = MutableStateFlow(Success)
    val status: StateFlow<QueryStatus> get() = _status

    fun updateStatus(queryStatus: QueryStatus) {
        _status.value = queryStatus
    }

    fun fillFilteredList(list: List<T>) {
        _filteredList.value = list
    }

    fun getFilteredList(): List<T> {
        return filteredList.value
    }

    fun clearFilteredList() {
        _filteredList.value = listOf()
    }

    companion object {
        const val ITEMS_PER_PAGE = 20
    }
}
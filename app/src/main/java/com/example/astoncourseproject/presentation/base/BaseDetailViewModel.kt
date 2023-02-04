package com.example.astoncourseproject.presentation.base

import androidx.lifecycle.ViewModel
import com.example.astoncourseproject.presentation.models.QueryStatus
import com.example.astoncourseproject.presentation.models.Success
import com.example.astoncourseproject.utils.getIdByUrlString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BaseDetailViewModel : ViewModel() {

    private val _status: MutableStateFlow<QueryStatus> = MutableStateFlow(Success)
    val status: StateFlow<QueryStatus> get() = _status

    fun updateStatus(queryStatus: QueryStatus) {
        _status.value = queryStatus
    }

    fun getIdListFromSomethingUrl(urlList: List<String>, separator: String): List<Int> {
        val resultArray = mutableListOf<Int>()
        urlList.forEach {
            val id = it.getIdByUrlString(separator)?.toInt()
            if (id != null) {
                resultArray.add(id)
            }
        }
        return resultArray
    }
}
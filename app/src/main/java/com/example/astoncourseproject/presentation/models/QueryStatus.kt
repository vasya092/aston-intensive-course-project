package com.example.astoncourseproject.presentation.models

sealed class QueryStatus
object Success : QueryStatus()
object Loading: QueryStatus()
object Failure: QueryStatus()
object CacheFailure: QueryStatus()

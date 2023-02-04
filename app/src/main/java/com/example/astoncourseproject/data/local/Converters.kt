package com.example.astoncourseproject.data.local

import androidx.room.TypeConverter
import com.example.astoncourseproject.domain.models.LocationLinkDomain
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken

class Converters {
    @TypeConverter
    fun fromString(value: String): List<String> {
        val listType = object : TypeToken<List<String>>() {

        }.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromArrayList(list: List<String>): String {
        val gson = Gson()
        return gson.toJson(list)
    }

    @TypeConverter
    fun locationFromString(value: String): LocationLinkDomain {
        val objectType = object:TypeToken<LocationLinkDomain>() {}.type
        return Gson().fromJson(value, objectType)
    }

    @TypeConverter
    fun fromLocationLinkDomain(item: LocationLinkDomain?): String? {
        val gson = Gson()
        return gson.toJson(item)
    }
}
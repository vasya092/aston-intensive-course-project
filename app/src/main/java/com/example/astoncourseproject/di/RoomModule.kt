package com.example.astoncourseproject.di

import android.content.Context
import com.example.astoncourseproject.data.local.LocalDatabase
import com.example.astoncourseproject.data.local.LocalDatabaseDao
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class RoomModule {
    @Provides
    @Singleton
    fun provideLocalDatabase(context: Context): LocalDatabase =
        LocalDatabase.getDatabase(context)

    @Provides
    @Singleton
    fun providesLocalDatabaseDao(localDatabase: LocalDatabase): LocalDatabaseDao = localDatabase.localDataBaseDao()
}
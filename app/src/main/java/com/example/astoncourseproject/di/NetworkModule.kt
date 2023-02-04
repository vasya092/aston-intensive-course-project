package com.example.astoncourseproject.di

import com.example.astoncourseproject.data.repositories.CharacterRepositoryImpl
import com.example.astoncourseproject.data.local.LocalDatabaseDao
import com.example.astoncourseproject.data.network.MortyService
import com.example.astoncourseproject.data.repositories.EpisodeRepositoryImpl
import com.example.astoncourseproject.data.repositories.LocationRepositoryImpl
import com.example.astoncourseproject.domain.CharacterRepository
import com.example.astoncourseproject.domain.EpisodeRepository
import com.example.astoncourseproject.domain.LocationRepository
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
class NetworkModule {

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideBaseApi(retrofit: Retrofit): MortyService {
        return retrofit.create(MortyService::class.java)
    }

    @Provides
    @Singleton
    fun provideCharactersRepository(
        mortyService: MortyService,
        localDatabaseDao: LocalDatabaseDao,
    ): CharacterRepository = CharacterRepositoryImpl(
        mortyService, localDatabaseDao
    )

    @Provides
    @Singleton
    fun provideLocationsRepository(
        mortyService: MortyService,
        localDatabaseDao: LocalDatabaseDao,
    ): LocationRepository = LocationRepositoryImpl(
        mortyService, localDatabaseDao
    )

    @Provides
    @Singleton
    fun provideEpisodesRepository(
        mortyService: MortyService,
        localDatabaseDao: LocalDatabaseDao,
    ): EpisodeRepository = EpisodeRepositoryImpl(
        mortyService, localDatabaseDao
    )

    companion object {
        const val BASE_URL = "https://rickandmortyapi.com/api/"
    }

}
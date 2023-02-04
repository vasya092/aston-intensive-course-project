package com.example.astoncourseproject.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.astoncourseproject.presentation.viewmodel.CharacterListViewModel
import com.example.astoncourseproject.presentation.viewmodel.EpisodeDetailViewModel
import com.example.astoncourseproject.presentation.viewmodel.EpisodesListViewModel
import com.example.astoncourseproject.presentation.viewmodel.LocationsListViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {
    @Binds
    fun bindViewModelFactory(viewModelFactory: ViewModelFactory): ViewModelProvider.Factory

    @Binds
    @IntoMap
    @ViewModelKey(CharacterListViewModel::class)
    fun bindCharactersListViewModel(charactersViewModel: CharacterListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(LocationsListViewModel::class)
    fun bindLocationsListViewModel(locationsListViewModel: LocationsListViewModel): ViewModel

    @Binds
    @IntoMap
    @ViewModelKey(EpisodesListViewModel::class)
    fun bindEpisodesListViewModel(episodesListViewModel: EpisodesListViewModel): ViewModel

//    @Binds
//    @IntoMap
//    @ViewModelKey(EpisodeDetailViewModel::class)
//    fun bindEpisodesDetailViewModel(episodeDetailViewModel: EpisodeDetailViewModel): ViewModel
}
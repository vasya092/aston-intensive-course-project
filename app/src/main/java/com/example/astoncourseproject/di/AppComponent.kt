package com.example.astoncourseproject.di

import android.content.Context
import com.example.astoncourseproject.MainActivity
import com.example.astoncourseproject.presentation.fragments.*
import com.example.astoncourseproject.presentation.fragments.bottomsheets.CharactersFilterBottomSheet
import com.example.astoncourseproject.presentation.fragments.bottomsheets.EpisodesFilterBottomSheet
import com.example.astoncourseproject.presentation.fragments.bottomsheets.LocationsFilterBottomSheet
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ViewModelModule::class, NetworkModule::class, RoomModule::class])
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun inject(activity: MainActivity)
    fun inject(charactersListFragment: CharactersListFragment)
    fun inject(locationsListFragment: LocationsListFragment)
    fun inject(charactersFilterBottomSheet: CharactersFilterBottomSheet)
    fun inject(characterDetailFragment: CharacterDetailFragment)
    fun inject(locationsFilterBottomSheet: LocationsFilterBottomSheet)
    fun inject(episodesListFragment: EpisodesListFragment)
    fun inject(episodesFilterBottomSheet: EpisodesFilterBottomSheet)
    fun inject(episodesDetailFragment: EpisodeDetailFragment)
    fun inject(locationDetailFragment: LocationDetailFragment)
}
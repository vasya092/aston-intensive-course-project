package com.example.astoncourseproject.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.example.astoncourseproject.BaseApplication
import com.example.astoncourseproject.R
import com.example.astoncourseproject.databinding.FragmentCharacterDetailBinding
import com.example.astoncourseproject.presentation.adapters.EpisodesListAdapter
import com.example.astoncourseproject.presentation.base.BaseFragment
import com.example.astoncourseproject.presentation.models.*
import com.example.astoncourseproject.presentation.viewmodel.CharacterDetailViewModel
import com.example.astoncourseproject.presentation.viewmodel.CharacterDetailViewModelFactory
import com.example.astoncourseproject.utils.getIdByUrlString
import kotlinx.coroutines.launch
import javax.inject.Inject

class CharacterDetailFragment : BaseFragment<FragmentCharacterDetailBinding>(
    FragmentCharacterDetailBinding::inflate) {

    private val viewModel: CharacterDetailViewModel by viewModels {
        factory.create(arguments?.getInt(ARGS_CHARACTER_ID) ?: 0)
    }

    @Inject
    lateinit var factory: CharacterDetailViewModelFactory.Factory

    lateinit var adapter: EpisodesListAdapter

    override fun onAttach(context: Context) {
        (activity?.application as BaseApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.coroutineScope.launch {
            viewModel.character.collect {
                bindCharacter(it)
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.episodes.collect {
                bindEpisodes(it)
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.status.collect() { status ->
                statusUpdateHandler(status)
            }
        }
    }

    private fun bindEpisodes(episodes: List<EpisodeItem>) {
        val recyclerGridLayout = GridLayoutManager(requireContext(), 2)
        adapter = EpisodesListAdapter {
            showItemDetail(it.id.toString(),
                EpisodeDetailFragment(),
                EpisodeDetailFragment.makeArgs(it.id.toInt()))
        }
        with(binding) {
            episodesRecycler.layoutManager = recyclerGridLayout
            adapter.submitList(episodes)
            episodesRecycler.adapter = adapter
        }
    }

    private fun bindCharacter(character: CharacterDetail) {
        binding.apply {
            characterDetailName.text = character.name
            characterDetailStatus.text = character.status
            characterDetailSpecies.text = character.species
            characterDetailGender.text = character.gender
            characterDetailType.text = character.type
            characterDetailLocation.apply {
                text =
                    context.getString(R.string.character_detail_location, character.location.name)
                setOnClickListener {
                    openDetailFragment(character.location.url.getIdByUrlString("location")
                        .toString())
                }
            }
            characterDetailOrigin.apply {
                text = context.getString(R.string.character_detail_origin, character.origin.name)
                setOnClickListener {
                    openDetailFragment(character.origin.url.getIdByUrlString("location").toString())
                }
            }
            Glide.with(requireContext()).load(character.image).into(characterDetailAvatar)
        }
    }

    private fun openDetailFragment(id: String?) {
        var bundle: Bundle? = null
        if (!id.isNullOrEmpty()) {
            bundle = LocationDetailFragment.makeArgs(id.toInt())
        }
        showItemDetail(id, LocationDetailFragment(), bundle)
    }

    companion object {
        private const val ARGS_CHARACTER_ID = "characterId"
        fun makeArgs(id: Int?): Bundle {
            return Bundle(1).apply {
                putInt(ARGS_CHARACTER_ID, id ?: 0)
            }
        }
    }
}
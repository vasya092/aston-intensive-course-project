package com.example.astoncourseproject.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.astoncourseproject.BaseApplication
import com.example.astoncourseproject.databinding.FragmentEpisodeDetailBinding
import com.example.astoncourseproject.presentation.adapters.CharacterListAdapter
import com.example.astoncourseproject.presentation.base.BaseFragment
import com.example.astoncourseproject.presentation.models.Character
import com.example.astoncourseproject.presentation.models.EpisodeDetail
import com.example.astoncourseproject.presentation.viewmodel.EpisodeDetailViewModel
import com.example.astoncourseproject.presentation.viewmodel.EpisodeDetailViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodeDetailFragment : BaseFragment<FragmentEpisodeDetailBinding>(
    FragmentEpisodeDetailBinding::inflate) {

    private val viewModel: EpisodeDetailViewModel by viewModels {
        factory.create(arguments?.getInt(ARGS_EPISODE_ID) ?: 0)
    }

    @Inject
    lateinit var factory: EpisodeDetailViewModelFactory.Factory

    private lateinit var adapter: CharacterListAdapter

    override fun onAttach(context: Context) {
        (activity?.application as BaseApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.coroutineScope.launch {
            viewModel.episode.collect {
                bindEpisode(it)
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.characters.collect {
                bindCharacters(it)
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.status.collect { status ->
                statusUpdateHandler(status)
            }
        }
    }

    private fun bindCharacters(characters: List<Character>) {
        val recyclerGridLayout = GridLayoutManager(requireContext(), 2)
        adapter = CharacterListAdapter {
            showItemDetail(it.id.toString(),
                CharacterDetailFragment(),
                CharacterDetailFragment.makeArgs(it.id.toInt()))
        }
        with(binding) {
            charactersRecycler.layoutManager = recyclerGridLayout
            adapter.submitList(characters)
            charactersRecycler.adapter = adapter
        }
    }

    private fun bindEpisode(episode: EpisodeDetail) {
        binding.apply {
            episodeDetailName.text = episode.name
            episodeDetailDate.text = episode.airDate
            episodeDetailNum.text = episode.episode
        }
    }

    companion object {
        private const val ARGS_EPISODE_ID = "episodeId"
        fun makeArgs(id: Int): Bundle {
            return Bundle(1).apply {
                putInt(ARGS_EPISODE_ID, id)
            }
        }
    }
}
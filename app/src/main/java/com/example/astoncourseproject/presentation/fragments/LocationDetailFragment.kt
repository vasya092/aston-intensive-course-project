package com.example.astoncourseproject.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import com.example.astoncourseproject.BaseApplication
import com.example.astoncourseproject.databinding.FragmentLocationDetailBinding
import com.example.astoncourseproject.presentation.adapters.CharacterListAdapter
import com.example.astoncourseproject.presentation.base.BaseFragment
import com.example.astoncourseproject.presentation.models.Character
import com.example.astoncourseproject.presentation.models.LocationDetail
import com.example.astoncourseproject.presentation.viewmodel.LocationDetailViewModel
import com.example.astoncourseproject.presentation.viewmodel.LocationDetailViewModelFactory
import kotlinx.coroutines.launch
import javax.inject.Inject

class LocationDetailFragment : BaseFragment<FragmentLocationDetailBinding>(
    FragmentLocationDetailBinding::inflate) {

    private val viewModel: LocationDetailViewModel by viewModels {
        factory.create(arguments?.getInt(ARGS_LOCATION_ID) ?: 0)
    }

    @Inject
    lateinit var factory: LocationDetailViewModelFactory.Factory

    private lateinit var adapter: CharacterListAdapter

    override fun onAttach(context: Context) {
        (activity?.application as BaseApplication).appComponent.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycle.coroutineScope.launch {
            viewModel.location.collect {
                bindLocation(it)
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

    private fun bindLocation(location: LocationDetail) {
        binding.apply {
            locationDetailName.text = location.name
            locationDetailType.text = location.type
            locationDetailDimension.text = location.dimension
        }
    }

    companion object {
        private const val ARGS_LOCATION_ID = "locationId"
        fun makeArgs(id: Int?): Bundle {
            return Bundle(1).apply {
                putInt(ARGS_LOCATION_ID, id ?: 0)
            }
        }
    }
}
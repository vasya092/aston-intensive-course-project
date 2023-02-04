package com.example.astoncourseproject.presentation.fragments

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.coroutineScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.astoncourseproject.BaseApplication
import com.example.astoncourseproject.databinding.FragmentEpisodesListBinding
import com.example.astoncourseproject.di.ViewModelFactory
import com.example.astoncourseproject.presentation.adapters.EpisodesListAdapter
import com.example.astoncourseproject.presentation.base.BaseListFragment
import com.example.astoncourseproject.presentation.fragments.bottomsheets.EpisodesFilterBottomSheet
import com.example.astoncourseproject.presentation.viewmodel.EpisodesListViewModel
import com.example.astoncourseproject.utils.onDebouncedChange
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

class EpisodesListFragment : BaseListFragment<FragmentEpisodesListBinding>(
    FragmentEpisodesListBinding::inflate
) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: EpisodesListViewModel by activityViewModels { viewModelFactory }

    private lateinit var adapter: EpisodesListAdapter
    private lateinit var listLayoutManager: GridLayoutManager

    override fun onAttach(context: Context) {
        val component = (requireActivity().application as BaseApplication).appComponent
        component.inject(this)
        super.onAttach(context)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        listLayoutManager = GridLayoutManager(requireContext(), 2)
        adapter = EpisodesListAdapter {
            showItemDetail(it.id.toString(),
                EpisodeDetailFragment(),
                EpisodeDetailFragment.makeArgs(it.id.toInt()))
        }

        binding.listRecycler.layoutManager = listLayoutManager
        binding.listRecycler.adapter = adapter

        errorHandler()
        flowHandler()
        filteredListFlowHandler()
        searchFieldFlowHandler()
        binding.searchField.setText(viewModel.searchField.value)

        addEndOfListListener(listLayoutManager)

        binding.searchField.onDebouncedChange(CoroutineScope(coroutineContext)) {
            findByName(it)
        }

        binding.clearFilterButton.setOnClickListener {
            clearFilteredList()
        }
    }

    private fun findByName(name: String) {
        if (!viewModel.isFilterActive.value) {
            viewModel.onFindByNameWithoutFilter(name)
        } else {
            findItemWithFilter(1)
        }
        viewModel.onUpdateSearchField(name)
    }

    private fun flowHandler() {
        lifecycle.coroutineScope.launch {
            viewModel.itemsFlow.collect { list ->
                if (!viewModel.isFilterActive.value) {
                    adapter.submitList(list)
                }
                searchFieldFlowHandler()
            }
        }
    }

    private fun findItemWithFilter(page: Int? = viewModel.nextPage) {
        viewModel.onFindWithFilter(
            binding.searchField.text.toString(),
            viewModel.episodesFilter,
            page
        )
    }

    private fun errorHandler() {
        lifecycle.coroutineScope.launch {
            viewModel.status.collect { status ->
                statusUpdateHandler(status)
            }
        }
    }

    private fun filteredListFlowHandler() {
        lifecycle.coroutineScope.launch {
            viewModel.filteredList.collect { list ->
                if (list.isNotEmpty()) {
                    adapter.submitList(list)
                } else {
                    adapter.submitList(viewModel.itemsFlow.value)
                }
            }
        }

        lifecycle.coroutineScope.launch {
            viewModel.isFilterActive.collect {
                if (!it) {
                    binding.clearFilterButton.visibility = View.GONE
                } else {
                    binding.clearFilterButton.visibility = View.VISIBLE
                }
            }
        }
    }

    private fun searchFieldFlowHandler() {
        lifecycle.coroutineScope.launch {
            viewModel.searchField.collect {
                if (it == "") {
                    binding.searchField.setText("")
                }
            }
        }
    }

    private fun addEndOfListListener(layoutManager: GridLayoutManager) {
        binding.listRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                val lastPosition = layoutManager.findLastVisibleItemPosition()
                if (lastPosition == adapter.itemCount - 1) {
                    viewModel.onListScrolled()
                }
            }
        })
    }

    override fun filterClickHandler() {
        EpisodesFilterBottomSheet().show(childFragmentManager, "")
    }

    private fun clearFilteredList() {
        viewModel.clearFilter()
        adapter.submitList(viewModel.itemsFlow.value) {
            binding.listRecycler.smoothScrollToPosition(0)
        }
    }
}
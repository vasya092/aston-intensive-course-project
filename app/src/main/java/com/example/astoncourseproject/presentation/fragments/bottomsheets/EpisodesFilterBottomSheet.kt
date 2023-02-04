package com.example.astoncourseproject.presentation.fragments.bottomsheets

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.astoncourseproject.BaseApplication
import com.example.astoncourseproject.databinding.FragmentEpisodesFilterBottomSheetBinding
import com.example.astoncourseproject.di.ViewModelFactory
import com.example.astoncourseproject.presentation.base.BaseFilterBottomSheetFragment
import com.example.astoncourseproject.presentation.models.FilterItem
import com.example.astoncourseproject.presentation.viewmodel.EpisodesListViewModel
import javax.inject.Inject

class EpisodesFilterBottomSheet: BaseFilterBottomSheetFragment<FragmentEpisodesFilterBottomSheetBinding>(
    FragmentEpisodesFilterBottomSheetBinding::inflate
) {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: EpisodesListViewModel by activityViewModels { viewModelFactory }

    override fun onAttach(context: Context) {
        val component = (requireActivity().application as BaseApplication).appComponent
        component.inject(this)
        super.onAttach(context)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFields()
        binding.bottomSheetFilterButton.setOnClickListener{
            onFilterClicked()
        }
    }

    private fun initFields() {
        with(binding) {
            episodeNumberInput.setText(viewModel.episodesFilter.value)
        }
    }

    private fun onFilterClicked() {
        with(binding) {
            val episode = FilterItem(value = episodeNumberInput.text.toString())
            viewModel.onFindWithFilter(viewModel.searchField.value, episode)
        }
        viewModel.onClearSearchField()
        dismiss()
    }
}
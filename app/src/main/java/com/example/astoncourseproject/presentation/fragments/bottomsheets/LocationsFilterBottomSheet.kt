package com.example.astoncourseproject.presentation.fragments.bottomsheets

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.astoncourseproject.BaseApplication
import com.example.astoncourseproject.databinding.FragmentLocationsFilterBottomSheetBinding
import com.example.astoncourseproject.di.ViewModelFactory
import com.example.astoncourseproject.presentation.base.BaseFilterBottomSheetFragment
import com.example.astoncourseproject.presentation.models.FilterItem
import com.example.astoncourseproject.presentation.viewmodel.LocationsListViewModel
import javax.inject.Inject

class LocationsFilterBottomSheet: BaseFilterBottomSheetFragment<FragmentLocationsFilterBottomSheetBinding>(
    FragmentLocationsFilterBottomSheetBinding::inflate
) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: LocationsListViewModel by activityViewModels { viewModelFactory }

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
            typeInput.setText(viewModel.typeFilter.value)
        }
    }

    private fun onFilterClicked() {
        with(binding) {
            val type = FilterItem(value = typeInput.text.toString())
            val dimension = FilterItem(value = dimenInput.text.toString())
            viewModel.onFindWithFilter(viewModel.searchField.value, type, dimension)
        }
        viewModel.onClearSearchField()
        dismiss()
    }
}
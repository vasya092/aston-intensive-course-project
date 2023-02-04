package com.example.astoncourseproject.presentation.fragments.bottomsheets

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.astoncourseproject.BaseApplication
import com.example.astoncourseproject.R
import com.example.astoncourseproject.databinding.FragmentCharactersFilterBottomSheetBinding
import com.example.astoncourseproject.di.ViewModelFactory
import com.example.astoncourseproject.presentation.base.BaseFilterBottomSheetFragment
import com.example.astoncourseproject.presentation.models.FilterItem
import com.example.astoncourseproject.presentation.viewmodel.CharacterListViewModel
import com.example.astoncourseproject.utils.fillFromArray
import com.example.astoncourseproject.utils.getFilterItem
import javax.inject.Inject

class CharactersFilterBottomSheet: BaseFilterBottomSheetFragment<FragmentCharactersFilterBottomSheetBinding>(
    FragmentCharactersFilterBottomSheetBinding::inflate
) {

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModel: CharacterListViewModel by activityViewModels { viewModelFactory }

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
        context?.let {
            with(binding) {
                statusesSelector.apply {
                    fillFromArray(it, R.array.statuses_array)
                    setSelection(viewModel.selectedStatusFilter.itemPosition)
                }
                speciesSelector.apply {
                    fillFromArray(it, R.array.species_array)
                    setSelection(viewModel.selectedSpeciesFilter.itemPosition)
                }
                gendersSelector.apply {
                    fillFromArray(it, R.array.genders_array)
                    setSelection(viewModel.selectedGenderFilter.itemPosition)
                }
            }
        }
    }

    private fun onFilterClicked() {
        // TODO Сделать нулевой итем плейсхолдером и добавить сюда конверт в ""
        with(binding) {
            val species = speciesSelector.getFilterItem()
            val status = statusesSelector.getFilterItem()
            val gender = gendersSelector.getFilterItem()
            val type = FilterItem(value = typeInput.text.toString())
            viewModel.onFindWithFilter(viewModel.searchField.value,status, species, gender, type)
        }
        viewModel.onClearSearchField()
        dismiss()
    }
}
package com.example.astoncourseproject.presentation.base

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.viewbinding.ViewBinding
import com.example.astoncourseproject.MainActivity
import com.example.astoncourseproject.R
import com.example.astoncourseproject.presentation.models.*
import com.example.astoncourseproject.utils.showSnackBar

typealias Inflate<T> = (LayoutInflater, ViewGroup?, Boolean) -> T

abstract class BaseFragment<VB : ViewBinding>(
    private val inflate: Inflate<VB>,
) : Fragment() {

    private var _binding: VB? = null
    protected val binding
        get() = requireNotNull(_binding) {
            "Binding isn't init"
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = inflate.invoke(inflater, container, false)
        return binding.root
    }

    fun showItemDetail(id: String?, fragment: Fragment, args: Bundle?) {
        if (!id.isNullOrEmpty()) {
            with(parentFragmentManager) {
                commit {
                    replace(R.id.fragment_container, fragment::class.java, args)
                    addToBackStack(fragment.javaClass.name)
                }
            }
        } else {
            binding.root.showSnackBar(getString(R.string.item_is_unknown_string))
        }

    }

    private fun showLoader() {
        (activity as MainActivity).showLoader()
    }

    private fun hideLoader() {
        (activity as MainActivity).hideLoader()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun statusUpdateHandler(status: QueryStatus) {
        when(status) {
            CacheFailure -> {
                hideLoader()
                binding.root.showSnackBar(getString(R.string.main_cache_data_failure_string))
            }
            Failure -> {
                hideLoader()
                binding.root.showSnackBar(getString(R.string.main_network_data_failure_string))
            }
            Loading -> showLoader()
            Success -> hideLoader()
        }
    }
}
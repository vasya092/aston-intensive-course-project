package com.example.astoncourseproject.presentation.base

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.view.MenuProvider
import androidx.fragment.app.*
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.astoncourseproject.R
import com.example.astoncourseproject.di.ViewModelFactory
import com.example.astoncourseproject.presentation.fragments.CharacterDetailFragment
import com.example.astoncourseproject.presentation.viewmodel.CharacterListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

abstract class BaseListFragment<VB : ViewBinding>(
    inflate: Inflate<VB>,
) : BaseFragment<VB>(inflate), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main

    private var clearMenuItem: MenuItem? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val menuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.toolbar_menu, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.filter_list -> {
                        filterClickHandler()
                        true
                    }
                    else -> {
                        true
                    }
                }
            }
        }, viewLifecycleOwner)
    }


    abstract fun filterClickHandler()
}
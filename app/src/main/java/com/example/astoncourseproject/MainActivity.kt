package com.example.astoncourseproject

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.addCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.example.astoncourseproject.databinding.ActivityMainBinding
import com.example.astoncourseproject.presentation.fragments.CharactersListFragment
import com.example.astoncourseproject.presentation.fragments.EpisodesListFragment
import com.example.astoncourseproject.presentation.fragments.LocationsListFragment
import com.example.astoncourseproject.presentation.fragments.SplashScreenFragment

class MainActivity : AppCompatActivity(), FragmentManager.OnBackStackChangedListener {

    private lateinit var binding: ActivityMainBinding

    private val charactersListFragment = CharactersListFragment()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val locationsListFragment = LocationsListFragment()
        val episodesListFragment = EpisodesListFragment()
        val splashScreenFragment = SplashScreenFragment()

        if(savedInstanceState == null) {
            loadFragment(splashScreenFragment, false)
            binding.bottomNavigation.visibility = View.GONE
            supportActionBar?.hide()
        }

        supportFragmentManager.addOnBackStackChangedListener(this)

        binding.bottomNavigation.setOnItemSelectedListener {
            when(it.itemId) {
                R.id.characters -> {
                    loadFragment(charactersListFragment)
                    true
                }
                R.id.locations -> {
                    loadFragment(locationsListFragment)
                    true
                }
                R.id.episodes -> {
                    loadFragment(episodesListFragment)
                    true
                }
                else -> true
            }
        }

        onBackPressedDispatcher.addCallback(this) {
            systemBackButtonHandler()
        }
    }

    fun showLoader() {
        binding.loaderContainer.visibility = View.VISIBLE
    }

    fun hideLoader() {
        binding.loaderContainer.visibility = View.INVISIBLE
    }

    fun startBaseScreen() {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id,charactersListFragment)
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
        transaction.commit()
        binding.bottomNavigation.visibility = View.VISIBLE
        supportActionBar?.show()
    }

    private fun systemBackButtonHandler() {
        if(supportFragmentManager.backStackEntryCount <= 0) {
            finish()
        } else {
            supportFragmentManager.popBackStack()
        }
    }

    private fun loadFragment(fragment: Fragment, isNeedBackstack: Boolean = true){
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(binding.fragmentContainer.id,fragment)
        if(isNeedBackstack) {
            transaction.addToBackStack(fragment::class.toString())
        }
        transaction.commit()
    }

    override fun onBackStackChanged() {
        if(supportFragmentManager.backStackEntryCount > 0) {
            supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        } else {
            supportActionBar!!.setDisplayHomeAsUpEnabled(false)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            supportFragmentManager.popBackStack()
            //TODO Сделать переключение bottom navigation
        }
        return super.onOptionsItemSelected(item)
    }
}
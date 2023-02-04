package com.example.astoncourseproject.presentation.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.lifecycle.lifecycleScope
import com.example.astoncourseproject.MainActivity
import com.example.astoncourseproject.databinding.FragmentSplashScreenBinding
import com.example.astoncourseproject.presentation.base.BaseFragment
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : BaseFragment<FragmentSplashScreenBinding>(
    FragmentSplashScreenBinding::inflate
) {
    private val splashScreenDelay = 2000L

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        lifecycleScope.launch {
            delay(splashScreenDelay)
            (activity as MainActivity?)?.startBaseScreen()

        }
    }
}
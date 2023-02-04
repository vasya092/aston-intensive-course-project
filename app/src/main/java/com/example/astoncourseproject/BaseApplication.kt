package com.example.astoncourseproject

import android.app.Application
import com.example.astoncourseproject.di.AppComponent
import com.example.astoncourseproject.di.DaggerAppComponent

open class BaseApplication: Application() {
    val appComponent: AppComponent by lazy {
        DaggerAppComponent.factory().create(applicationContext)
    }
}
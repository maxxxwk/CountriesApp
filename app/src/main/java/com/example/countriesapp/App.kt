package com.example.countriesapp

import android.app.Application
import com.example.countriesapp.di.AppComponent
import com.example.countriesapp.di.DaggerAppComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@ExperimentalCoroutinesApi
@FlowPreview
class App : Application() {
    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .appContext(this)
            .build()
    }
}

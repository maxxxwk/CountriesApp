package com.example.countriesapp.di

import android.content.Context
import com.example.countriesapp.MainActivity
import com.example.countriesapp.countries.presentation.CountriesFragment
import com.example.countriesapp.details.presenation.CountryDetailsFragment
import dagger.BindsInstance
import dagger.Component
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import javax.inject.Singleton

@ExperimentalCoroutinesApi
@FlowPreview
@Component(modules = [AppModule::class, NetworkModule::class, DatabaseModule::class, ViewModelModule::class])
@Singleton
interface AppComponent {

    fun inject(countriesFragment: CountriesFragment)

    fun inject(countryDetailsFragment: CountryDetailsFragment)

    fun inject(mainActivity: MainActivity)

    @Component.Builder
    interface Builder {
        @BindsInstance
        fun appContext(appContext: Context): Builder

        fun build(): AppComponent
    }
}

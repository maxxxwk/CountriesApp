package com.example.countriesapp.di

import androidx.lifecycle.ViewModel
import com.example.countriesapp.MainActivityViewModel
import com.example.countriesapp.common.viewmodel.ViewModelKey
import com.example.countriesapp.countries.presentation.CountriesFragmentViewModel
import com.example.countriesapp.details.presenation.CountryDetailsFragmentViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@Module
@ExperimentalCoroutinesApi
@FlowPreview
interface ViewModelModule {

    @Binds
    @ViewModelKey(CountriesFragmentViewModel::class)
    @IntoMap
    fun bindCountriesFragmentViewModel(countriesFragmentViewModel: CountriesFragmentViewModel): ViewModel


    @Binds
    @ViewModelKey(CountryDetailsFragmentViewModel::class)
    @IntoMap
    fun bindCountryDetailsFragmentViewModel(countryDetailsFragmentViewModel: CountryDetailsFragmentViewModel): ViewModel

    @Binds
    @ViewModelKey(MainActivityViewModel::class)
    @IntoMap
    fun bindMainActivityViewModel(mainActivityViewModel: MainActivityViewModel): ViewModel
}

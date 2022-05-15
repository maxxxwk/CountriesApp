package com.example.countriesapp.countries.presentation

import androidx.lifecycle.viewModelScope
import com.example.countriesapp.common.viewmodel.BaseViewModel
import com.example.countriesapp.countries.data.CountriesRepository
import com.example.countriesapp.utils.ResultWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class CountriesFragmentViewModel @Inject constructor(private val countriesRepository: CountriesRepository) :
    BaseViewModel<CountriesFragmentState, CountriesFragmentIntent>(CountriesFragmentState.Loading) {

    init {
        executeIntent(CountriesFragmentIntent.LoadCountries)
    }

    override fun handleIntent(intent: CountriesFragmentIntent): CountriesFragmentState =
        when (intent) {
            CountriesFragmentIntent.LoadCountries -> {
                loadCountries()
                CountriesFragmentState.Loading
            }
            is CountriesFragmentIntent.ShowCountries -> {
                CountriesFragmentState.Content(intent.countries)
            }
            is CountriesFragmentIntent.ShowError -> {
                CountriesFragmentState.Error(intent.errorMessage)
            }
        }

    private fun loadCountries() {
        countriesRepository.getCountriesListFlow()
            .onEach {
                when (it) {
                    is ResultWrapper.Fail -> executeIntent(
                        CountriesFragmentIntent.ShowError(it.errorMessage)
                    )
                    is ResultWrapper.Success -> executeIntent(
                        CountriesFragmentIntent.ShowCountries(it.value)
                    )
                }
            }.launchIn(viewModelScope)
    }
}

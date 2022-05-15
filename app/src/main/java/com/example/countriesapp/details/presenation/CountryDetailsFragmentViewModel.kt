package com.example.countriesapp.details.presenation

import androidx.lifecycle.viewModelScope
import com.example.countriesapp.common.viewmodel.BaseViewModel
import com.example.countriesapp.details.data.CountryDetailsRepository
import com.example.countriesapp.utils.ResultWrapper
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class CountryDetailsFragmentViewModel @Inject constructor(
    private val countryDetailsRepository: CountryDetailsRepository
) : BaseViewModel<CountryDetailsFragmentState, CountryDetailsFragmentIntent>(
    CountryDetailsFragmentState.PendingSelect
) {

    fun load(id: String) {
        executeIntent(CountryDetailsFragmentIntent.LoadCountryDetails(id))
    }

    override fun handleIntent(intent: CountryDetailsFragmentIntent): CountryDetailsFragmentState {
        return when (intent) {
            is CountryDetailsFragmentIntent.LoadCountryDetails -> {
                loadCountryDetails(intent.id)
                CountryDetailsFragmentState.Loading
            }
            is CountryDetailsFragmentIntent.ShowCountryDetails -> {
                CountryDetailsFragmentState.Content(intent.countryDetails)
            }
            is CountryDetailsFragmentIntent.ShowError -> {
                CountryDetailsFragmentState.Error(intent.errorMessage)
            }
        }
    }

    private fun loadCountryDetails(id: String) {
        countryDetailsRepository.getCountryDetailsFlow(id)
            .onEach {
                when (it) {
                    is ResultWrapper.Fail -> executeIntent(
                        CountryDetailsFragmentIntent.ShowError(it.errorMessage)
                    )
                    is ResultWrapper.Success -> executeIntent(
                        CountryDetailsFragmentIntent.ShowCountryDetails(it.value)
                    )
                }
            }.launchIn(viewModelScope)
    }
}

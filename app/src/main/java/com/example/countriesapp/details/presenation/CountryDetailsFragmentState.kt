package com.example.countriesapp.details.presenation

import com.example.countriesapp.details.domain.CountryDetails

sealed interface CountryDetailsFragmentState {
    object PendingSelect : CountryDetailsFragmentState
    object Loading : CountryDetailsFragmentState
    data class Content(val countryDetails: CountryDetails) : CountryDetailsFragmentState
    data class Error(val errorMessage: String) : CountryDetailsFragmentState
}

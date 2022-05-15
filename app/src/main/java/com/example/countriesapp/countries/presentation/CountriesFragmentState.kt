package com.example.countriesapp.countries.presentation

import com.example.countriesapp.countries.domain.Country

sealed interface CountriesFragmentState {
    object Loading : CountriesFragmentState
    data class Content(val countries: List<Country>) : CountriesFragmentState
    data class Error(val errorMessage: String) : CountriesFragmentState
}

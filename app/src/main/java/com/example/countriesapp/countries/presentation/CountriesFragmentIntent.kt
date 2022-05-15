package com.example.countriesapp.countries.presentation

import com.example.countriesapp.countries.domain.Country

sealed interface CountriesFragmentIntent {
    object LoadCountries : CountriesFragmentIntent
    data class ShowError(val errorMessage: String) : CountriesFragmentIntent
    data class ShowCountries(val countries: List<Country>) : CountriesFragmentIntent
}

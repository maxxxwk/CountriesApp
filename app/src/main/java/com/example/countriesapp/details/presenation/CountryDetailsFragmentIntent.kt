package com.example.countriesapp.details.presenation

import com.example.countriesapp.details.domain.CountryDetails

sealed interface CountryDetailsFragmentIntent {
    data class LoadCountryDetails(val id: String): CountryDetailsFragmentIntent
    data class ShowError(val errorMessage: String): CountryDetailsFragmentIntent
    data class ShowCountryDetails(val countryDetails: CountryDetails): CountryDetailsFragmentIntent
}

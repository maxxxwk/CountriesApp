package com.example.countriesapp.details.domain

data class CountryDetails(
    val name: String,
    val nativeName: String,
    val emoji: String,
    val capital: String,
    val currency: String,
    val phoneCode: String,
    val continent: String
)

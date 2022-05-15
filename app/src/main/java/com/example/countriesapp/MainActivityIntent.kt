package com.example.countriesapp

sealed interface MainActivityIntent {
    data class SelectCountry(val id: String) : MainActivityIntent
    object Unselect : MainActivityIntent
}

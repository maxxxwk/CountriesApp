package com.example.countriesapp

sealed interface MainActivityState {
    object NotSelected : MainActivityState
    data class Selected(val id: String) : MainActivityState
}

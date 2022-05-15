package com.example.countriesapp

import com.example.countriesapp.common.viewmodel.BaseViewModel
import javax.inject.Inject

class MainActivityViewModel @Inject constructor() :
    BaseViewModel<MainActivityState, MainActivityIntent>(MainActivityState.NotSelected) {

    override fun handleIntent(intent: MainActivityIntent): MainActivityState {
        return when (intent) {
            is MainActivityIntent.SelectCountry -> MainActivityState.Selected(intent.id)
            MainActivityIntent.Unselect -> MainActivityState.NotSelected
        }
    }

    fun selectCountry(id: String) {
        executeIntent(MainActivityIntent.SelectCountry(id))
    }

    fun unselect() {
        executeIntent(MainActivityIntent.Unselect)
    }
}

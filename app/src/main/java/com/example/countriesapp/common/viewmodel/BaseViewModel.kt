package com.example.countriesapp.common.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.*

abstract class BaseViewModel<STATE, INTENT>(initialState: STATE) : ViewModel() {

    private val _state: MutableStateFlow<STATE> = MutableStateFlow(initialState)
    val state: StateFlow<STATE>
        get() = _state.asStateFlow()

    private val intentsFlow = MutableSharedFlow<INTENT>(replay = 1)

    init {
        intentsFlow.onEach {
            _state.value = handleIntent(it)
        }.launchIn(viewModelScope)
    }

    protected abstract fun handleIntent(intent: INTENT): STATE

    protected fun executeIntent(intent: INTENT) {
        intentsFlow.tryEmit(intent)
    }
}

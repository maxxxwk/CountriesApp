package com.example.countriesapp.countries.data

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.example.countriesapp.GetCountriesListQuery
import com.example.countriesapp.R
import com.example.countriesapp.common.network.NetworkTracker
import com.example.countriesapp.common.resources.ResourceManager
import com.example.countriesapp.countries.data.db.CountriesDao
import com.example.countriesapp.countries.data.db.models.CountryEntity
import com.example.countriesapp.countries.data.db.models.toCountryEntity
import com.example.countriesapp.countries.domain.Country
import com.example.countriesapp.di.DispatcherIO
import com.example.countriesapp.utils.ResultWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class CountriesRepository @Inject constructor(
    private val countriesDao: CountriesDao,
    private val apolloClient: ApolloClient,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val networkTracker: NetworkTracker,
    private val resourceManager: ResourceManager
) {

    fun getCountriesListFlow(): Flow<ResultWrapper<List<Country>>> =
        getCountriesListNetworkRequestFlow().flatMapConcat {
            it?.let {
                updateCountriesInDatabase(it)
                flowOf(it)
            } ?: getCountriesListFlowWithUpdating()
        }.map(::mapToResult).flowOn(dispatcher)

    private fun getCountriesListFlowWithUpdating(): Flow<List<CountryEntity>> {
        var isLoadedFromNetwork = false

        return countriesDao.getCountries()
            .combine(loadCountriesWhenNetworkBecomeAvailable()) { countriesFromDB, countriesFromNetwork ->
                if (!isLoadedFromNetwork && countriesFromNetwork.isNotEmpty()) {
                    updateCountriesInDatabase(countriesFromNetwork)
                    isLoadedFromNetwork = true
                }
                countriesFromDB
            }.distinctUntilChanged()
    }

    private fun loadCountriesWhenNetworkBecomeAvailable() =
        networkTracker.networkStatus
            .filter { it }
            .flatMapConcat { getCountriesListNetworkRequestFlow() }
            .filterNotNull()
            .take(1)
            .onStart { emit(emptyList()) }

    private fun getCountriesListNetworkRequestFlow() = apolloClient
        .query(GetCountriesListQuery())
        .toFlow()
        .map { response ->
            response.data?.countries?.map(GetCountriesListQuery.Country::toCountryEntity)
        }.catch { emit(null) }

    private suspend fun updateCountriesInDatabase(countries: List<CountryEntity>) = coroutineScope {
        launch {
            try {
                countriesDao.insertCountries(countries)
            } catch (e: Exception) {
                Log.e(this::class.java.simpleName, e.message ?: "Unknown error")
            }
        }
    }

    private fun mapToResult(countries: List<CountryEntity>): ResultWrapper<List<Country>> =
        if (countries.isNotEmpty()) {
            ResultWrapper.Success(countries.map(CountryEntity::toCountry))
        } else {
            ResultWrapper.Fail(resourceManager.getString(R.string.empty_countries_list_error_message))
        }
}

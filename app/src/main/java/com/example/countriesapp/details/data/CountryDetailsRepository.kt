package com.example.countriesapp.details.data

import android.util.Log
import com.apollographql.apollo3.ApolloClient
import com.example.countriesapp.GetCountryDetailsQuery
import com.example.countriesapp.R
import com.example.countriesapp.common.network.NetworkTracker
import com.example.countriesapp.common.resources.ResourceManager
import com.example.countriesapp.details.data.db.CountriesDetailsDao
import com.example.countriesapp.details.data.db.models.CountryDetailsEntity
import com.example.countriesapp.details.data.db.models.toCountryDetailsEntity
import com.example.countriesapp.details.domain.CountryDetails
import com.example.countriesapp.di.DispatcherIO
import com.example.countriesapp.utils.ResultWrapper
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import java.lang.Exception
import javax.inject.Inject

@ExperimentalCoroutinesApi
@FlowPreview
class CountryDetailsRepository @Inject constructor(
    private val apolloClient: ApolloClient,
    private val countriesDetailsDao: CountriesDetailsDao,
    @DispatcherIO private val dispatcher: CoroutineDispatcher,
    private val networkTracker: NetworkTracker,
    private val resourceManager: ResourceManager
) {

    fun getCountryDetailsFlow(id: String): Flow<ResultWrapper<CountryDetails>> =
        getCountryDetailsNetworkRequestFlow(id).flatMapConcat {
            it?.let {
                updateCountryDetailsInDatabase(it)
                flowOf(it)
            } ?: getCountryDetailsFlowWithUpdating(id)
        }.map(::mapToResultWrapper).flowOn(dispatcher)

    private fun getCountryDetailsFlowWithUpdating(id: String): Flow<CountryDetailsEntity?> {
        var isLoadedFromNetwork = false

        return countriesDetailsDao.getCountryDetails(id)
            .combine(loadCountriesWhenNetworkBecomeAvailable(id)) { fromDB, fromNetwork ->
                if (!isLoadedFromNetwork && fromNetwork != null) {
                    updateCountryDetailsInDatabase(fromNetwork)
                    isLoadedFromNetwork = true
                }
                fromDB
            }.distinctUntilChanged()
    }

    private fun loadCountriesWhenNetworkBecomeAvailable(id: String) =
        networkTracker.networkStatus
            .filter { it }
            .flatMapConcat { getCountryDetailsNetworkRequestFlow(id) }
            .take(1)
            .onStart { emit(null) }

    private fun getCountryDetailsNetworkRequestFlow(id: String) = apolloClient
        .query(GetCountryDetailsQuery(id))
        .toFlow()
        .map { response ->
            response.data?.country?.toCountryDetailsEntity()
        }.catch { emit(null) }

    private suspend fun updateCountryDetailsInDatabase(countryDetailsEntity: CountryDetailsEntity) =
        coroutineScope {
            launch {
                try {
                    countriesDetailsDao.insertCountryDetails(countryDetailsEntity)
                } catch (e: Exception) {
                    Log.e(this::class.java.simpleName, e.message ?: "Unknown error")
                }
            }
        }

    private fun mapToResultWrapper(countryDetailsEntity: CountryDetailsEntity?): ResultWrapper<CountryDetails> {
        return countryDetailsEntity?.let {
            ResultWrapper.Success(it.toCountryDetails())
        } ?: ResultWrapper.Fail(
            resourceManager.getString(R.string.absent_country_details_error_message)
        )
    }

}

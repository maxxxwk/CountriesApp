package com.example.countriesapp.common.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkTracker @Inject constructor(context: Context) {
    private val connectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    @ExperimentalCoroutinesApi
    val networkStatus = callbackFlow {
        val networkStatusCallback = object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                trySend(true)
            }

            override fun onUnavailable() {
                trySend(false)
            }
        }
        connectivityManager.registerNetworkCallback(
            NetworkRequest.Builder()
                .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
                .build(),
            networkStatusCallback
        )

        awaitClose {
            connectivityManager.unregisterNetworkCallback(networkStatusCallback)
        }
    }
}

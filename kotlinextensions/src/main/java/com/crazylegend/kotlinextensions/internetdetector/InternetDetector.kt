package com.crazylegend.kotlinextensions.internetdetector

import android.Manifest.permission.ACCESS_NETWORK_STATE
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Build
import androidx.annotation.RequiresPermission
import androidx.lifecycle.LiveData
import com.crazylegend.kotlinextensions.context.connectivityManager
import com.crazylegend.kotlinextensions.context.isOnline


/**
 * Created by Hristijan on 2/1/19 to long live and prosper !
 */
class InternetDetector(private val context: Context) : LiveData<Boolean>() {

    private val connectivityManager = context.connectivityManager
    private val networkCallback: NetworkCallback = NetworkCallback(this)

    @RequiresPermission(allOf = [ACCESS_NETWORK_STATE])
    override fun onActive() {
        super.onActive()
        updateConnection()
        when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.N -> connectivityManager?.registerDefaultNetworkCallback(networkCallback)
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP -> {
                val builder = NetworkRequest.Builder()
                        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
                        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
                        .addTransportType(NetworkCapabilities.TRANSPORT_VPN)
                        .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
                connectivityManager?.registerNetworkCallback(builder.build(), networkCallback)
            }
        }
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager?.unregisterNetworkCallback(networkCallback)
    }

    private fun updateConnection() {
        postValue(context.isOnline)
    }

    class NetworkCallback(private val liveData: InternetDetector) : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            liveData.postValue(true)
        }

        override fun onLost(network: Network) {
            liveData.postValue(false)
        }
    }


}

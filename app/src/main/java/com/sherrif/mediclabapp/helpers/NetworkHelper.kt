package com.sherrif.mediclabapp.helpers

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

class NetworkHelper {
    companion object{
        fun checkForInternet(context: Context):Boolean{
            //register activity with the connectivity manager service
            val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        //this willwork for android M and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                val network = connectivityManager.activeNetwork ?: return false
                //check for active network capability
                val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false
                return when{
                    //indicate this network use a cellular transport
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI)-> true
                    //cellular network connectivity
                    activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ->true
                    else-> false
                }

            }else{
                //this android below version 7
                val networkInfo = connectivityManager.activeNetworkInfo ?:return false
                return networkInfo.isConnected

            }
        }
    }
}
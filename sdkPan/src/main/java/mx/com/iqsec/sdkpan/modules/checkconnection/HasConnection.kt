package mx.com.iqsec.sdkpan.modules.checkconnection

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

@SuppressLint("MissingPermission")
fun getInternet(activity: Activity): Boolean {
    var result = false
    val connectivityManager =
        activity.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw = connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> CheckInternetTask().execute()
                .get()

            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> CheckInternetTask().execute()
                .get()

            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> CheckInternetTask().execute()
                .get()

            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> CheckInternetTask().execute().get()
                    ConnectivityManager.TYPE_MOBILE -> CheckInternetTask().execute().get()
                    ConnectivityManager.TYPE_ETHERNET -> CheckInternetTask().execute().get()
                    else -> false
                }

            }
        }
    }

    return result
}
package mx.com.iqsec.sdkpan.common

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority

class SDK_LocationHelper(private val context: Context) {

    @SuppressLint("MissingPermission")
    fun getLocationWithFusedClient(hasGps: Boolean, callback: (Location?) -> Unit) {
        val permission = if (hasGps) {
            Manifest.permission.ACCESS_FINE_LOCATION
        } else {
            Manifest.permission.ACCESS_COARSE_LOCATION

        }

        if (ActivityCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(null) // Permiso no concedido
            return
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(null)
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

        // Primero intentamos obtener la última ubicación conocida (respuesta rápida)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    callback(location)
                } else {
                    requestFreshLocation(hasGps, fusedLocationClient, callback)
                }
            }
            .addOnFailureListener {
                requestFreshLocation(hasGps, fusedLocationClient, callback)
            }
    }

    @SuppressLint("MissingPermission")
    private fun requestFreshLocation(
        hasGps: Boolean,
        fusedLocationClient: com.google.android.gms.location.FusedLocationProviderClient,
        callback: (Location?) -> Unit
    ) {
        val permission = if (hasGps) {
            Manifest.permission.ACCESS_FINE_LOCATION
        } else {
            Manifest.permission.ACCESS_COARSE_LOCATION
        }
        val locationRequest = com.google.android.gms.location.LocationRequest.Builder(
            Priority.PRIORITY_BALANCED_POWER_ACCURACY,
            5000L // 5 segundos máximo de espera
        )
            .setMinUpdateIntervalMillis(0L)
            .setMaxUpdates(1)
            .build()

        if (ActivityCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(null) // Permiso no concedido
            return
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(null)
            return
        }

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(result: LocationResult) {
                    callback(result.lastLocation)
                    fusedLocationClient.removeLocationUpdates(this)
                }
            },
            Looper.getMainLooper()
        )
    }

    fun getLocationWithHuaweiClient(hasGps: Boolean, callback: (Location?) -> Unit) {
        val permission = if (hasGps) {
            Manifest.permission.ACCESS_FINE_LOCATION
        } else {
            Manifest.permission.ACCESS_COARSE_LOCATION
        }

        if (ActivityCompat.checkSelfPermission(
                context,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            callback(null)
            return
        }

        val fusedLocationClient =
            com.huawei.hms.location.LocationServices.getFusedLocationProviderClient(context)
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if (it != null) {
                callback(it)
            } else {
                Log.i("TAG", "getLastLocation onSuccess location is null")
            }
        }.addOnFailureListener { e ->
            Log.i("TAG", "getLastLocation onSuccess location is null")
        }
    }
}
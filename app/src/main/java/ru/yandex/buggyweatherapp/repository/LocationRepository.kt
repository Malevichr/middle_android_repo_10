package ru.yandex.buggyweatherapp.repository

import android.os.Looper
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.Priority
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import ru.yandex.buggyweatherapp.model.Location
import ru.yandex.buggyweatherapp.model.MyLocationResult
import javax.inject.Inject

interface LocationRepository {
    fun currentLocation(): Flow<MyLocationResult>
    class Impl @Inject constructor(
        private val fusedLocationClient: FusedLocationProviderClient
    ) : LocationRepository {

        override fun currentLocation(): Flow<MyLocationResult> = callbackFlow {
            val locationRequest =
                LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10_000L)
                    .setWaitForAccurateLocation(false)
                    .setMinUpdateIntervalMillis(5_000L)
                    .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val androidLocation = locationResult.lastLocation ?: return

                    val userLocation = Location(
                        latitude = androidLocation.latitude,
                        longitude = androidLocation.longitude
                    )

                    trySend(
                        MyLocationResult.Success(userLocation)
                    )
                }
            }

            try {
                fusedLocationClient.lastLocation
                    .addOnSuccessListener { androidLocation ->
                        if (androidLocation != null) {
                            val userLocation = Location(
                                latitude = androidLocation.latitude,
                                longitude = androidLocation.longitude
                            )

                            trySend(
                                MyLocationResult.Success(userLocation)
                            )

                            close()
                        } else {
                            fusedLocationClient.requestLocationUpdates(
                                locationRequest,
                                locationCallback,
                                Looper.getMainLooper()
                            )
                        }
                    }
                    .addOnFailureListener {
                        trySend(
                            MyLocationResult.Error(
                                "Error getting location"
                            )
                        )
                        close()
                    }
            } catch (_: SecurityException) {
                trySend(
                    MyLocationResult.Error(
                        "Location permission not granted"
                    )
                )
                close()
            }

            awaitClose {
                fusedLocationClient.removeLocationUpdates(locationCallback)
            }
        }
    }
}


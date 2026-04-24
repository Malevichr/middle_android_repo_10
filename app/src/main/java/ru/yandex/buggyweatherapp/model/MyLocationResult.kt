package ru.yandex.buggyweatherapp.model

sealed interface MyLocationResult {
    data class Success(val location: Location) : MyLocationResult
    data class Error(val message: String) : MyLocationResult
}
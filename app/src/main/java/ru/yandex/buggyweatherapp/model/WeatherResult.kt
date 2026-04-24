package ru.yandex.buggyweatherapp.model

sealed interface WeatherResult {
    data class Success(val weatherData: WeatherData) : WeatherResult
    data class Error(val message: String) : WeatherResult
}

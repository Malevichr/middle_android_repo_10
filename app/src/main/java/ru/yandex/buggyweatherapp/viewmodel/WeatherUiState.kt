package ru.yandex.buggyweatherapp.viewmodel

import ru.yandex.buggyweatherapp.model.WeatherData

sealed interface WeatherUiState {
    object Initial : WeatherUiState
    data class Success(val weatherData: WeatherData) : WeatherUiState
    data class Error(val exception: Exception) : WeatherUiState
    object Loading : WeatherUiState
}
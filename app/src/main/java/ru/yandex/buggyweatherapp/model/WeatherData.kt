package ru.yandex.buggyweatherapp.model

data class WeatherData(
    val cityName: String,
    val temperature: Double,
    val feelsLike: Double,
    val minTemp: Double,
    val maxTemp: Double,
    val humidity: Int,
    val pressure: Int,
    val windSpeed: Double,
    val description: String,
    val icon: String,
    val sunriseTime: Long,
    val sunsetTime: Long,
    val isFavorite: Boolean = false
)
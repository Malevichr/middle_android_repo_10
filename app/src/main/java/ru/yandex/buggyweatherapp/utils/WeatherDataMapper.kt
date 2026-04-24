package ru.yandex.buggyweatherapp.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object WeatherDataMapper {
    fun formatTimestamp(timestamp: Long): String {
        val date = Date(timestamp * 1000)
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        return sdf.format(date)
    }

    fun getWeatherDescription(description: String, temperature: Double): String {
        var result = ""
        result += description.replaceFirstChar { it.uppercase() }
        result += ", "
        result += "${temperature.toInt()}°C"
        return result
    }
    fun weatherIconUrl(icon: String): String {
        return "https://openweathermap.org/img/wn/${icon}@2x.png"
    }
    fun formatTemperature(temp: Double): String {
        return "${temp.toInt()}°C"
    }
}
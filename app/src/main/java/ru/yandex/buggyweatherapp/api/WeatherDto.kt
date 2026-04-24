package ru.yandex.buggyweatherapp.api

import com.google.gson.annotations.SerializedName

data class WeatherDto(
    val name: String,
    val main: MainDto,
    val wind: WindDto,
    val sys: SysDto,
    val weather: List<WeatherInfoDto>,
    val clouds: CloudsDto
)

data class MainDto(
    val temp: Double,
    @SerializedName("feels_like")
    val feelsLike: Double,
    @SerializedName("temp_min")
    val tempMin: Double,
    @SerializedName("temp_max")
    val tempMax: Double,
    val humidity: Int,
    val pressure: Int
)

data class WindDto(
    val speed: Double
)

data class SysDto(
    val sunrise: Long,
    val sunset: Long
)

data class WeatherInfoDto(
    val description: String,
    val icon: String
)

data class CloudsDto(
    val all: Int
)
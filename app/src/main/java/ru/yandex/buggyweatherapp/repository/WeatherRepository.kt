package ru.yandex.buggyweatherapp.repository

import retrofit2.Response
import ru.yandex.buggyweatherapp.api.WeatherApiService
import ru.yandex.buggyweatherapp.api.WeatherDto
import ru.yandex.buggyweatherapp.model.Location
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.model.WeatherResult
import javax.inject.Inject

interface WeatherRepository {
    suspend fun weatherByLocation(location: Location): WeatherResult
    suspend fun weatherByCity(cityName: String): WeatherResult

    class Impl @Inject constructor(
        private val weatherApi: WeatherApiService
    ) : WeatherRepository {

        override suspend fun weatherByCity(cityName: String): WeatherResult {
            return mapResponse { weatherApi.getWeatherByCity(cityName) }
        }

        override suspend fun weatherByLocation(location: Location): WeatherResult {
            return mapResponse {
                weatherApi.getCurrentWeather(
                    location.latitude,
                    location.longitude
                )
            }
        }

        private suspend fun mapResponse(
            call: suspend () -> Response<WeatherDto>
        ): WeatherResult {
            return try {
                val response = call()

                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null) {
                        WeatherResult.Success(body.toWeatherData())
                    } else {
                        WeatherResult.Error("Empty response")
                    }
                } else {
                    WeatherResult.Error("API Error: ${response.code()}")
                }
            } catch (_: Exception) {
                WeatherResult.Error("Error fetching weather")
            }
        }

        private fun WeatherDto.toWeatherData(
            isFavorite: Boolean = false
        ): WeatherData {
            val weatherInfo = weather.firstOrNull()

            return WeatherData(
                cityName = name,
                temperature = main.temp,
                feelsLike = main.feelsLike,
                minTemp = main.tempMin,
                maxTemp = main.tempMax,
                humidity = main.humidity,
                pressure = main.pressure,
                windSpeed = wind.speed,
                description = weatherInfo?.description.orEmpty(),
                icon = weatherInfo?.icon.orEmpty(),
                sunriseTime = sys.sunrise,
                sunsetTime = sys.sunset,
                isFavorite = isFavorite
            )
        }
    }
}
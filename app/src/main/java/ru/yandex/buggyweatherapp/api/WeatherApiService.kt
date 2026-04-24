package ru.yandex.buggyweatherapp.api

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import ru.yandex.buggyweatherapp.BuildConfig

interface WeatherApiService {
    @GET("weather")
    suspend fun getCurrentWeather(
        @Query("lat") latitude: Double,
        @Query("lon") longitude: Double,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): Response<WeatherDto>

    @GET("weather")
    suspend fun getWeatherByCity(
        @Query("q") cityName: String,
        @Query("appid") apiKey: String = API_KEY,
        @Query("units") units: String = "metric"
    ): Response<WeatherDto>

    companion object {
        const val API_KEY = BuildConfig.API_KEY
        const val BASE_URL = "http://api.openweathermap.org/data/2.5/"
    }

}
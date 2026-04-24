package ru.yandex.buggyweatherapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import ru.yandex.buggyweatherapp.model.MyLocationResult
import ru.yandex.buggyweatherapp.model.WeatherResult
import ru.yandex.buggyweatherapp.repository.LocationRepository
import ru.yandex.buggyweatherapp.repository.WeatherRepository
import javax.inject.Inject

@HiltViewModel
class WeatherViewModel @Inject constructor(
    private val locationRepository: LocationRepository,
    private val weatherRepository: WeatherRepository
) : ViewModel() {

    private val _state = MutableStateFlow<WeatherUiState>(WeatherUiState.Initial)
    val state: StateFlow<WeatherUiState> = _state
    private var weatherJob: Job? = null
    fun fetchCurrentLocationWeather() {
        weatherJob?.cancel()
        weatherJob = locationRepository.currentLocation()
            .onStart {
                _state.update {
                    WeatherUiState.Loading
                }
            }
            .onEach { locationResult ->
                when (locationResult) {
                    is MyLocationResult.Error -> _state.update { WeatherUiState.Error(locationResult.message) }

                    is MyLocationResult.Success -> {
                        val weather = weatherRepository.weatherByLocation(locationResult.location)
                        when (weather) {
                            is WeatherResult.Error -> _state.update { WeatherUiState.Error(weather.message) }
                            is WeatherResult.Success -> _state.update {
                                WeatherUiState.Success(
                                    weather.weatherData
                                )
                            }
                        }
                    }
                }
            }.launchIn(
                scope = viewModelScope
            )
    }

    fun searchWeatherByCity(city: String) {
        weatherJob?.cancel()

        if (city.isBlank()) {
            _state.update {
                WeatherUiState.Error("City name cannot be empty")
            }
            return
        }
        _state.update {
            WeatherUiState.Loading
        }
        weatherJob = viewModelScope.launch(Dispatchers.IO) {
            when (val weather = weatherRepository.weatherByCity(city)) {
                is WeatherResult.Error -> _state.update { WeatherUiState.Error(weather.message) }
                is WeatherResult.Success -> _state.update {
                    WeatherUiState.Success(
                        weather.weatherData
                    )
                }
            }
        }
    }
}
package ru.yandex.buggyweatherapp.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import ru.yandex.buggyweatherapp.repository.LocationRepository
import ru.yandex.buggyweatherapp.repository.WeatherRepository

@Module
@InstallIn(ViewModelComponent::class)
interface WeatherModule {
    @Binds
    fun bindLocationRepository(repository: LocationRepository.Impl) : LocationRepository
    @Binds
    fun bindWeatherRepository(repository: WeatherRepository.Impl) : WeatherRepository
}
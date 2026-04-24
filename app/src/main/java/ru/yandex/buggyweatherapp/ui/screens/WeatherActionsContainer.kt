package ru.yandex.buggyweatherapp.ui.screens

data class WeatherActionsContainer(
    val onSearchTextChange: (String) -> Unit = {},
    val onSearch: () -> Unit = {},
    val onFavoriteClick: () -> Unit = {},
    val onRefreshClick: () -> Unit = {},
    val onLocationRequest: () -> Unit = {},
    val onCardClick: () -> Unit = {},
)
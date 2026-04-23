package ru.yandex.buggyweatherapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.android.gms.location.LocationRequest
import okio.IOException
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.ui.components.DetailedWeatherCard
import ru.yandex.buggyweatherapp.ui.components.LocationSearch
import ru.yandex.buggyweatherapp.ui.components.WeatherCard
import ru.yandex.buggyweatherapp.viewmodel.WeatherUiState
import ru.yandex.buggyweatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    var searchText by rememberSaveable { mutableStateOf("") }
    var showDetailedCard by rememberSaveable { mutableStateOf(false) }
    WeatherScreenUi(
        state = state.value,
        searchText = searchText,
        showDetailedCard = showDetailedCard,
        actions = WeatherActionsContainer(
            onSearchTextChange = { searchText = it },
            onSearch = { viewModel.searchWeatherByCity(searchText) },
            onFavoriteClick = viewModel::toggleFavorite,
            onRefreshClick = { viewModel.searchWeatherByCity(searchText) },
            onLocationRequest = viewModel::fetchCurrentLocationWeather,
            onCardClick = { showDetailedCard = !showDetailedCard },
        ),
        modifier = modifier
    )
}

data class WeatherActionsContainer(
    val onSearchTextChange: (String) -> Unit = {},
    val onSearch: () -> Unit = {},
    val onFavoriteClick: () -> Unit = {},
    val onRefreshClick: () -> Unit = {},
    val onLocationRequest: () -> Unit = {},
    val onCardClick: () -> Unit = {},
)

@Composable
fun WeatherScreenUi(
    state: WeatherUiState,
    searchText: String,
    showDetailedCard: Boolean,
    actions: WeatherActionsContainer,
    modifier: Modifier = Modifier
) = with(actions) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LocationSearch(
            searchText = searchText,
            onSearchChange = onSearchTextChange,
            onCitySearch = onSearch,
            onLocationRequest = onLocationRequest
        )
        Spacer(modifier = Modifier.height(16.dp))

        when (state) {
            is WeatherUiState.Initial -> {}
            is WeatherUiState.Error -> {
                Text(
                    text = state.exception.message.toString(),
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(8.dp)
                )
            }

            is WeatherUiState.Loading -> Text("Loading weather data...")
            is WeatherUiState.Success ->
                if (showDetailedCard)
                    DetailedWeatherCard(
                        state.weatherData,
                        onFavoriteClick = onFavoriteClick,
                        onRefreshClick = onRefreshClick,
                        onCardClick = onCardClick
                    )
                else WeatherCard(
                    weather = state.weatherData,
                    onFavoriteClick = onFavoriteClick,
                    onRefreshClick = onRefreshClick,
                    onCardClick = onCardClick
                )
        }
    }
}


@Preview
@Composable
private fun WeatherScreenUiInitialPreview() {
    WeatherScreenUi(
        state = WeatherUiState.Initial,
        searchText = "",
        showDetailedCard = false,
        actions = WeatherActionsContainer()
    )
}

@Preview
@Composable
private fun WeatherScreenUiErrorPreview() {
    WeatherScreenUi(
        state = WeatherUiState.Error(IOException("Internet error")),
        searchText = "Surgut",
        showDetailedCard = false,
        actions = WeatherActionsContainer()
    )
}

@Preview
@Composable
private fun WeatherScreenUiLoadingPreview() {
    WeatherScreenUi(
        state = WeatherUiState.Loading,
        searchText = "Surgut",
        showDetailedCard = false,
        actions = WeatherActionsContainer()
    )
}

@Preview
@Composable
private fun WeatherScreenUiSuccessPreview() {
    WeatherScreenUi(
        state = WeatherUiState.Success(
            WeatherData(
                cityName = "Surgut",
                temperature = 2.0,
                feelsLike = 0.0,
                minTemp = -1.0,
                maxTemp = 4.0,
                humidity = 80,
                pressure = 750,
                windSpeed = 5.2,
                description = "description",
                icon = "",
                sunriseTime = 15L,
                sunsetTime = 12L,
                isFavorite = false
            )
        ),
        searchText = "Surgut",
        showDetailedCard = false,
        actions = WeatherActionsContainer()
    )
}

@Preview
@Composable
private fun WeatherScreenUiSuccessDetailedPreview() {
    WeatherScreenUi(
        state = WeatherUiState.Success(
            WeatherData(
                cityName = "Surgut",
                temperature = 2.0,
                feelsLike = 0.0,
                minTemp = -1.0,
                maxTemp = 4.0,
                humidity = 80,
                pressure = 750,
                windSpeed = 5.2,
                description = "description",
                icon = "",
                sunriseTime = 15L,
                sunsetTime = 12L,
                isFavorite = false
            )
        ),
        searchText = "Surgut",
        showDetailedCard = true,
        actions = WeatherActionsContainer()
    )
}
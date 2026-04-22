package ru.yandex.buggyweatherapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import okio.IOException
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.utils.WeatherIconMapper
import ru.yandex.buggyweatherapp.viewmodel.WeatherUiState
import ru.yandex.buggyweatherapp.viewmodel.WeatherViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(viewModel: WeatherViewModel, modifier: Modifier = Modifier) {

    val state = viewModel.state.collectAsStateWithLifecycle()

    var searchText by rememberSaveable { mutableStateOf("") }

    WeatherScreenUi(
        state.value,
        searchText = searchText,
        onSearchTextChange = { searchText = it },
        onSearch = { viewModel.searchWeatherByCity(searchText) },
        onFavoriteClick = viewModel::toggleFavorite,
        onRefreshClick = { viewModel.searchWeatherByCity(searchText) },
        modifier = modifier
    )

}

@Composable
fun WeatherScreenUi(
    state: WeatherUiState,
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onSearch: () -> Unit,
    onFavoriteClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { onSearchTextChange(it) },
            label = { Text("Search city") },
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                IconButton(onClick = {
                    onSearch()
                }) {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                onSearch()
            })
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
            is WeatherUiState.Success -> WeatherCard(
                weather = state.weatherData,
                onFavoriteClick = onFavoriteClick,
                onRefreshClick = onRefreshClick
            )
        }
    }
}

@Composable
fun WeatherCard(
    weather: WeatherData,
    onFavoriteClick: () -> Unit,
    onRefreshClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = weather.cityName,
                    style = MaterialTheme.typography.headlineMedium
                )

                Row {
                    IconButton(onClick = onFavoriteClick) {
                        Icon(
                            imageVector = if (weather.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                            contentDescription = "Favorite"
                        )
                    }

                    IconButton(onClick = onRefreshClick) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Temperature: " + weather.temperature.toString() + "°C",
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Feels like: " + weather.feelsLike.toString() + "°C",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Description: " + weather.description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Humidity: " + weather.humidity.toString() + "%",
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = "Wind: " + weather.windSpeed.toString() + " m/s",
                style = MaterialTheme.typography.bodyMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = "Sunrise: " + WeatherIconMapper.formatTimestamp(weather.sunriseTime),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Sunset: " + WeatherIconMapper.formatTimestamp(weather.sunsetTime),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRefreshClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Refresh Weather")
            }
        }
    }
}

@Preview
@Composable
private fun WeatherScreenUiInitialPreview() {
    WeatherScreenUi(
        state = WeatherUiState.Initial,
        searchText = "",
        onSearchTextChange = { },
        onSearch = {},
        onFavoriteClick = {},
        onRefreshClick = {},
    )
}

@Preview
@Composable
private fun WeatherScreenUiErrorPreview() {
    WeatherScreenUi(
        state = WeatherUiState.Error(IOException("Internet error")),
        searchText = "Surgut",
        onSearchTextChange = { },
        onSearch = {},
        onFavoriteClick = {},
        onRefreshClick = {},
    )
}

@Preview
@Composable
private fun WeatherScreenUiLoadingPreview() {
    WeatherScreenUi(
        state = WeatherUiState.Loading,
        searchText = "Surgut",
        onSearchTextChange = { },
        onSearch = {},
        onFavoriteClick = {},
        onRefreshClick = {},
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
        onSearchTextChange = { },
        onSearch = {},
        onFavoriteClick = {},
        onRefreshClick = {},
    )
}
package ru.yandex.buggyweatherapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import ru.yandex.buggyweatherapp.R
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.utils.WeatherDataMapper

@Composable
fun DetailedWeatherCard(
    weather: WeatherData,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = weather.cityName,
                style = MaterialTheme.typography.headlineMedium
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 8.dp)
            ) {
                AsyncImage(
                    model = WeatherDataMapper.weatherIconUrl(weather.icon),
                    contentDescription = WeatherDataMapper.getWeatherDescription(
                        weather.description,
                        weather.temperature
                    ),
                    modifier = Modifier.size(50.dp),
                )

                Text(
                    text = WeatherDataMapper.formatTemperature(weather.temperature),
                    style = MaterialTheme.typography.headlineLarge
                )
            }

            Text(
                text = weather.description.replaceFirstChar { it.uppercase() },
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.height(16.dp))


            LazyColumn {
                item {
                    WeatherDataRow(
                        stringResource(R.string.feels_like),
                        WeatherDataMapper.formatTemperature(weather.feelsLike)
                    )
                }
                item {
                    WeatherDataRow(
                        stringResource(R.string.min_max),
                        "${WeatherDataMapper.formatTemperature(weather.minTemp)} / " +
                                WeatherDataMapper.formatTemperature(weather.maxTemp)
                    )
                }
                item {
                    WeatherDataRow(
                        stringResource(R.string.humidity),
                        weather.humidity.toString() + "%"
                    )
                }
                item {
                    WeatherDataRow(
                        stringResource(R.string.pressure),
                        weather.pressure.toString() + " hPa"
                    )
                }
                item {
                    WeatherDataRow(
                        stringResource(R.string.wind),
                        weather.windSpeed.toString() + " m/s"
                    )
                }
                item {
                    WeatherDataRow(
                        "Sunrise",
                        WeatherDataMapper.formatTimestamp(weather.sunriseTime)
                    )
                }
                item {
                    WeatherDataRow("Sunset", WeatherDataMapper.formatTimestamp(weather.sunsetTime))
                }
            }
        }
    }
}

@Composable
private fun WeatherDataRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
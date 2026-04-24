package ru.yandex.buggyweatherapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import ru.yandex.buggyweatherapp.R
import ru.yandex.buggyweatherapp.model.WeatherData
import ru.yandex.buggyweatherapp.utils.WeatherDataMapper

@Composable
fun WeatherCard(
    weather: WeatherData,
    onRefreshClick: () -> Unit,
    onCardClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onCardClick)
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


                    IconButton(onClick = onRefreshClick) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = stringResource(R.string.refresh_weather)
                        )
                    }
            }

            Spacer(modifier = Modifier.height(8.dp))


            Text(
                text = "Temperature: " + WeatherDataMapper.formatTemperature(weather.temperature) ,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                text = "Feels like: " + WeatherDataMapper.formatTemperature(weather.feelsLike),
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
                    text = "Sunrise: " + WeatherDataMapper.formatTimestamp(weather.sunriseTime),
                    style = MaterialTheme.typography.bodySmall
                )

                Text(
                    text = "Sunset: " + WeatherDataMapper.formatTimestamp(weather.sunsetTime),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = onRefreshClick,
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text(stringResource(R.string.refresh_weather))
            }
        }
    }
}
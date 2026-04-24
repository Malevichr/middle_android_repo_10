package ru.yandex.buggyweatherapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import ru.yandex.buggyweatherapp.R

@Composable
fun LocationSearch(
    searchText: String,
    onSearchChange: (String) -> Unit,
    onCitySearch: () -> Unit,
    onLocationRequest: () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { onSearchChange(it) },
            label = { Text(stringResource(R.string.search_city)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            leadingIcon = {
                IconButton(onClick = { onLocationRequest() }) {
                    Icon(Icons.Default.LocationOn, contentDescription = stringResource(R.string.get_current_location))
                }
            },
            trailingIcon = {
                IconButton(onClick = {
                    if (searchText.isNotBlank()) {
                        onCitySearch()
                    }
                }) {
                    Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search))
                }
            },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions(onSearch = {
                if (searchText.isNotBlank()) {
                    onCitySearch()
                }
            })
        )
    }
}
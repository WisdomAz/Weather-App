package com.example.weatherapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

//function to format UNIX
fun formatDate(timestamp: Long): String {
    val instant = Instant.ofEpochSecond(timestamp)
    val formatter = DateTimeFormatter.ofPattern("EEE, MMM d").withZone(ZoneId.systemDefault())
    return formatter.format(instant)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForecastScreen(
    zipCode: String,
    onBack: () -> Unit,
    forecastViewModel: ForecastViewModel = viewModel()
) {

    LaunchedEffect(zipCode) {
        forecastViewModel.fetchForecast(zipCode)
    }


    val forecastResponse by forecastViewModel.forecast.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Forecast for $zipCode") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (forecastResponse != null) {
                // Display vertical list
                Column(modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = "16-Day Forecast for ${forecastResponse!!.city.name}",
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    LazyColumn {
                        items(forecastResponse!!.list) { day ->
                            ForecastDayItem(day)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            } else {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
fun ForecastDayItem(day: ForecastDay) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Date: ${formatDate(day.dt)}", fontSize = 16.sp)
            Text(text = "Day Temp: ${day.temp.day}°C", fontSize = 16.sp)
            Text(
                text = "Low: ${day.temp.min}°C   High: ${day.temp.max}°C",
                fontSize = 16.sp
            )
            if (day.weather.isNotEmpty()) {
                Text(text = "Condition: ${day.weather[0].main}", fontSize = 16.sp)
            }
            Text(text = "Humidity: ${day.humidity}%   Pressure: ${day.pressure} hPa", fontSize = 16.sp)
        }
    }
}

package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Button
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WeatherScreen(weatherViewModel: WeatherViewModel = viewModel()) {
    val weather by weatherViewModel.weather.observeAsState()
    var zipCode by remember { mutableStateOf("") }

    // Trigger fetching of current weather data
    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather("Saint Paul")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "My Weather App",
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Zip Code Input Section
            OutlinedTextField(
                value = zipCode,
                onValueChange = { newValue ->
                    if (newValue.length <= 5 && newValue.all { it.isDigit() }) {
                        zipCode = newValue
                    }
                },
                label = { Text("Enter Zip Code") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (zipCode.length == 5) {
                        // TODO: Implement navigation to forecast screen with this zip code
                        println("Navigate to forecast screen with zip code: $zipCode")
                    } else {
                        println("Please enter a valid 5-digit zip code")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("View 16-Day Forecast")
            }
            Spacer(modifier = Modifier.height(16.dp))

            // Current Weather Display
            weather?.let { data ->
                Text(
                    text = data.name,
                    fontSize = 24.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${data.main.temp.toInt()}°",
                            fontSize = 64.sp
                        )
                        Text(
                            text = "Feels like ${(data.main.temp + 6).toInt()}°",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(text = "Low: ${data.main.temp_min}°")
                        Text(text = "High: ${data.main.temp_max}°")
                        Text(text = "Humidity: ${data.main.humidity}%")
                        Text(text = "Pressure: ${data.main.pressure} hPa")
                    }
                    Image(
                        painter = painterResource(id = R.drawable.sun),
                        contentDescription = "Weather Icon",
                        modifier = Modifier.size(220.dp)
                    )
                }
            } ?: Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Text("Fetching weather...")
            }
        }
    }
}

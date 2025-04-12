package com.example.weatherapp

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.livedata.observeAsState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentConditionsScreen(
    onForecastClick: (String) -> Unit,
    weatherViewModel: WeatherViewModel = viewModel()
) {
    var zipCode by remember { mutableStateOf("") }
    val context = LocalContext.current

    // Fetch current weather
    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather("Saint Paul")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(id = R.string.app_name),
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
            OutlinedTextField(
                value = zipCode,
                onValueChange = { newValue ->
                    if (newValue.length <= 5 && newValue.all { it.isDigit() }) {
                        zipCode = newValue
                    }
                },
                label = { Text(text = stringResource(id = R.string.enter_zip)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = {
                    if (zipCode.length == 5) {
                        onForecastClick(zipCode)
                    } else {
                        Toast.makeText(
                            context,
                            context.getString(R.string.invalid_zip),
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = stringResource(id = R.string.view_forecast))
            }
            Spacer(modifier = Modifier.height(16.dp))

            val weather by weatherViewModel.weather.observeAsState()
            if (weather != null) {
                Column {
                    Text(text = weather!!.name, fontSize = 24.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "${weather!!.main.temp.toInt()}°", fontSize = 64.sp)
                            Text(text = "Feels like ${(weather!!.main.temp + 6).toInt()}°", fontSize = 16.sp)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(text = "Low: ${weather!!.main.temp_min}°")
                            Text(text = "High: ${weather!!.main.temp_max}°")
                            Text(text = "Humidity: ${weather!!.main.humidity}%")
                            Text(text = "Pressure: ${weather!!.main.pressure} hPa")
                        }
                        Image(
                            painter = painterResource(id = R.drawable.sun),
                            contentDescription = "Weather Icon",
                            modifier = Modifier.size(220.dp)
                        )
                    }
                }
            } else {
                Text(
                    text = stringResource(id = R.string.fetching_weather),
                    fontSize = 16.sp
                )
            }
        }
    }
}

package com.example.weatherapp

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.style.TextAlign
import com.example.weatherapp.R

@Composable
fun WeatherScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // App Title
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 24.sp
        )

        Spacer(modifier = Modifier.height(16.dp))


        Image(
            painter = painterResource(id = R.drawable.sun),
            contentDescription = stringResource(id = R.string.weather_description),
            modifier = Modifier.size(100.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = stringResource(id = R.string.weather_condition),
            fontSize = 18.sp
        )

        // Temperature
        Text(
            text = stringResource(id = R.string.temperature),
            fontSize = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))


        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 16.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = "Low: ${stringResource(id = R.string.low)}",
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )
            Text(
                text = "High: ${stringResource(id = R.string.high)}",
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )
            Text(
                text = "Humidity: ${stringResource(id = R.string.humidity)}",
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )
            Text(
                text = "Pressure: ${stringResource(id = R.string.pressure)}",
                fontSize = 14.sp,
                textAlign = TextAlign.Start
            )
        }
    }
}

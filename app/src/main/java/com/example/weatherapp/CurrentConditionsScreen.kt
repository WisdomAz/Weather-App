package com.example.weatherapp

import android.Manifest
import android.content.Intent
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
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
    val context = LocalContext.current

    // pull these into variables so we don't call stringResource inside semantics { ... }
    val enterZipDesc       = stringResource(R.string.enter_zip)
    val viewForecastDesc   = stringResource(R.string.view_forecast)
    val myLocationDesc     = stringResource(R.string.my_location)
    val locationDeniedMsg  = stringResource(R.string.location_permission_denied)
    val notificationDenied = stringResource(R.string.notification_permission_denied)
    val invalidZipMsg      = stringResource(R.string.invalid_zip)
    val fetchingDesc       = stringResource(R.string.fetching_weather)

    var zipCode by remember { mutableStateOf("") }
    val weatherState by weatherViewModel.weather.observeAsState()

    val locationLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            Intent(context, WeatherLocationService::class.java).also { intent ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
                    context.startForegroundService(intent)
                else
                    context.startService(intent)
            }
        } else {
            Toast.makeText(context, locationDeniedMsg, Toast.LENGTH_SHORT).show()
        }
    }

    val notifLauncher = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            if (!granted)
                Toast.makeText(context, notificationDenied, Toast.LENGTH_SHORT).show()
        }
    } else null

    LaunchedEffect(Unit) {
        weatherViewModel.fetchWeather("Saint Paul")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Box(Modifier.fillMaxWidth()) {
                        Text(
                            text = stringResource(R.string.app_name),
                            modifier = Modifier.align(Alignment.Center),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = zipCode,
                    onValueChange = { new ->
                        if (new.length <= 5 && new.all(Char::isDigit)) zipCode = new
                    },
                    label = { Text(enterZipDesc) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier
                        .weight(1f)
                        .semantics { contentDescription = enterZipDesc }
                )
                Spacer(Modifier.width(8.dp))
                IconButton({
                    locationLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)
                    notifLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                }) {
                    Icon(
                        imageVector = Icons.Filled.LocationOn,
                        contentDescription = myLocationDesc
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            Button(
                onClick = {
                    if (zipCode.length == 5) onForecastClick(zipCode)
                    else Toast.makeText(context, invalidZipMsg, Toast.LENGTH_SHORT).show()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .semantics { contentDescription = viewForecastDesc }
            ) {
                Text(viewForecastDesc)
            }

            Spacer(Modifier.height(16.dp))

            if (weatherState != null) {
                val data = weatherState!!
                Column {
                    Text(data.name, fontSize = 24.sp)
                    Spacer(Modifier.height(8.dp))
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text("${data.main.temp.toInt()}°", fontSize = 64.sp)
                            Text("Feels like ${(data.main.temp + 6).toInt()}°", fontSize = 16.sp)
                            Spacer(Modifier.height(16.dp))
                            Text(stringResource(R.string.low, data.main.temp_min))
                            Text(stringResource(R.string.high, data.main.temp_max))
                            Text(stringResource(R.string.humidity, data.main.humidity))
                            Text(stringResource(R.string.pressure, data.main.pressure))
                        }
                        Image(
                            painter = painterResource(R.drawable.sun),
                            contentDescription = stringResource(R.string.weather_description),
                            modifier = Modifier.size(220.dp)
                        )
                    }
                }
            } else {
                Text(fetchingDesc, fontSize = 16.sp)
            }
        }
    }
}

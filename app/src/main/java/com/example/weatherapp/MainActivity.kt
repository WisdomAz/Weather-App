package com.example.weatherapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import com.example.weatherapp.ui.theme.WeatherAppTheme
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            WeatherAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "current_conditions"
                    ) {
                        composable("current_conditions") {
                            CurrentConditionsScreen(
                                onForecastClick = { zipCode ->
                                    navController.navigate("forecast/$zipCode")
                                }
                            )
                        }
                        composable("forecast/{zip}") { backStackEntry ->
                            val zip = backStackEntry.arguments?.getString("zip") ?: ""
                            ForecastScreen(
                                zipCode = zip,
                                onBack = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}

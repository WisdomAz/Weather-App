package com.example.weatherapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.*
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class WeatherViewModelTest {

    // Make LiveData execute synchronously
    @get:Rule val instantRule = InstantTaskExecutorRule()

    // Swap Dispatchers.Main for tests
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: WeatherViewModel
    private val fakeApi = mockk<WeatherApi>()

    @Before fun setup() {
        Dispatchers.setMain(testDispatcher)
        // Inject a client that uses fakeApi
        viewModel = WeatherViewModel(weatherApi = fakeApi)
    }

    @After fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test fun `fetchWeather on success posts value`() = runTest {
        val sample = WeatherResponse(
            weather = listOf(WeatherCondition("Clear","clear sky")),
            main = MainWeather(20.0,50,1012),
            name = "Testville"
        )
        coEvery { fakeApi.getWeather(any(), any(), any()) } returns sample

        viewModel.fetchWeather("Testville")
        testDispatcher.scheduler.advanceUntilIdle()

        val result = viewModel.weather.value
        assertEquals("Testville", result?.name)
        assertEquals(20.0, result?.main?.temp)
    }

    @Test fun `fetchWeather on failure posts null`() = runTest {
        coEvery { fakeApi.getWeather(any(), any(), any()) } throws RuntimeException()

        viewModel.fetchWeather("Nowhere")
        testDispatcher.scheduler.advanceUntilIdle()

        assertNull(viewModel.weather.value)
    }
}

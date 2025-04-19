package com.example.weatherapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ForecastScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test fun forecastList_displaysItems() {
        // Navigate to forecast screen first, e.g. by entering zip + clicking button
        rule.onNodeWithContentDescription("Enter Zip Code")
            .performTextInput("55127")
        rule.onNodeWithText("View 16‑Day Forecast").performClick()

        // Now verify at least one list item is shown
        rule.onNode(hasText("Low:")).assertExists()
    }
}

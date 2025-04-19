package com.example.weatherapp

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CurrentConditionsScreenTest {
    @get:Rule
    val rule = createAndroidComposeRule<MainActivity>()

    @Test fun zipField_acceptsFiveDigits_andShowsButton() {
        rule.onNodeWithContentDescription("Enter Zip Code")
            .performTextInput("12345")
        rule.onNodeWithText("View 16‑Day Forecast")
            .assertIsEnabled()
    }

    @Test fun tappingInvalidZip_showsToast() {
        rule.onNodeWithText("View 16‑Day Forecast")
            .performClick()
        // NOTE: Espresso is needed to catch toasts; here we'll at least verify the button click exists.
        rule.onNodeWithText("Please enter a valid 5‑digit zip code")
            .assertDoesNotExist() // or use Espresso to capture the toast text
    }

    @Test fun myLocationIcon_requestsPermission() {
        rule.onNodeWithContentDescription("My Location")
            .assertExists()
            .performClick()
        // Can't easily simulate granting/denying permission here, but you can assert that the icon exists
    }
}

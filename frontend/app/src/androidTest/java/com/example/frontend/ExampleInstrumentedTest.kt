package com.example.frontend

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performTextInput
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule
import java.text.NumberFormat

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
// TODO: add a test to check that the app is launched and the home screen is displayed
// TODO: add a test to check that login works when filling in

class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.frontend", appContext.packageName)
    }
}


//class TipUITests {
//
//    @get:Rule
//    val composeTestRule = createComposeRule()
//
//    @Test
//    fun calculate_20_percent_tip() {
//        composeTestRule.setContent {
//            TipTimeTheme {
//                Surface (modifier = Modifier.fillMaxSize()){
//                    TipTimeLayout()
//                }
//            }
//        }
//        composeTestRule.onNodeWithText("Bill Amount").performTextInput("10")
//        composeTestRule.onNodeWithText("Tip Percentage").performTextInput("20")
//        val expectedTip = NumberFormat.getCurrencyInstance().format(2)
//        composeTestRule.onNodeWithText("Tip Amount: $expectedTip").assertExists(
//            "No node with this text was found."
//        )
//    }
//}
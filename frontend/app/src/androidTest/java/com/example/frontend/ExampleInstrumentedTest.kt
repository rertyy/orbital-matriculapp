package com.example.frontend

import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.assertHasClickAction
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.onRoot
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.printToLog
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.frontend.ui.screens.EventsScreen
import com.example.frontend.ui.screens.LoginScreen

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */

// TODO: add a test to check that the app is launched and the home screen is displayed
// TODO: add a test to check that login works when filling in
/*
class ExampleInstrumentedTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.frontend", appContext.packageName)
    }
}
*/


@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {

    @get:Rule
    val rule = createComposeRule()

    @Test
    fun printSemantics() {
        rule.setContent {
            MainApp()
        }
        // Log the full semantics tree
        rule.onRoot().printToLog("MY TAG")
    }


    @Test
    fun `mainUiScreenTest`() {
        rule.setContent { EventsScreen() }   //Note: HomeScreen declaration altered
        rule.onNodeWithText("MatriculApp")
            .assertExists()
        rule.onNodeWithText("Deadlines")
            .assertExists()
        rule.onNodeWithText("Reminders")
            .assertExists()
        rule.onNodeWithText("Upcoming Events")
            .assertExists()
    }

    @Test
    fun navbarIconsExistsTest() {
        rule.setContent { MainApp() }

        rule.onNode(
            hasContentDescription("Home"),
            useUnmergedTree = true
        ).assertExists()
        rule.onNode(
            hasContentDescription("Forum"),
            useUnmergedTree = true
        ).assertExists()
        rule.onNode(
            hasContentDescription("Login"),
            useUnmergedTree = true
        ).assertExists()

    }

    @Test
    fun navbarCanBeClicked() {
        rule.setContent { MainApp() }

        rule.onNode(
            hasClickAction()
                    and hasTestTag("Home")
        ).assertHasClickAction()
        rule.onNode(
            hasClickAction()
                    and hasTestTag("Forum")
        ).assertHasClickAction()
        rule.onNode(
            hasClickAction()
                    and hasTestTag("Login")
        ).assertHasClickAction()

    }

    @Test
    fun navbarLoginButtonWorks() {
        rule.setContent { MainApp() }
        rule.onNode(
            hasClickAction()
                    and hasTestTag("Login")
        ).performClick()

        rule.onNodeWithText("Click here to login").assertExists()

    }

    @Test
    fun homeButtonWorks() {
        rule.setContent { MainApp() }
        rule.onNode(
            hasClickAction()
                    and hasTestTag("Login")
        ).performClick()
        rule.onNode(
            hasClickAction()
                    and hasTestTag("Home")
        ).performClick()

        rule.onNodeWithText("MatriculApp")
            .assertExists()
        rule.onNodeWithText("Deadlines")
            .assertExists()
        rule.onNodeWithText("Reminders")
            .assertExists()
        rule.onNodeWithText("Upcoming Events")
            .assertExists()

    }


    @Test
    fun loginButtonsExists() {
        rule.setContent { LoginScreen {} }

        rule.onNode(
            hasClickAction() and hasTestTag("Username_button")
        ).assertHasClickAction()

        rule.onNode(
            hasClickAction() and hasTestTag("Password_button")
        ).assertHasClickAction()

        rule.onNode(hasClickAction() and hasText("login")).assertHasClickAction()
    }

    @Test
    fun showPasswordIconExists() {
        rule.setContent { LoginScreen {} }

        rule.onNode(
            hasContentDescription("Show password"), useUnmergedTree = true
        ).assertExists()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginSucceeds() {
        rule.setContent { LoginScreen {} }

        rule.onNode(
            hasClickAction() and hasTestTag("Username_button")
        ).performClick().performTextInput("user3")

        rule.onNode(
            hasClickAction() and hasTestTag("Password_button")
        ).performClick().performTextInput("password")

        rule.onNode(hasClickAction() and hasText("login")).performClick()


        rule.waitUntilExactlyOneExists(hasText("Login error"), 20_000)

        rule.onNodeWithText("Login error").assertDoesNotExist()

    }

    //tests login failure
    @OptIn(ExperimentalTestApi::class)
    @Test
    fun loginFails() {
        rule.setContent { LoginScreen {} }

        rule.onNode(
            hasClickAction() and hasTestTag("Username_button")
        ).performClick().performTextInput("sadpath")

        rule.onNode(
            hasClickAction() and hasTestTag("Password_button")
        ).performClick().performTextInput("sadsadsad")

        rule.onNode(hasClickAction() and hasText("login")).performClick()


        rule.waitUntilExactlyOneExists(hasText("Login error"), 20_000)

        rule.onNodeWithText("Login error").assertExists()

    }

    @Test
    fun showPasswordButtonWorks() {
        rule.setContent { LoginScreen {} }

        rule.onNode(
            hasClickAction() and hasTestTag("Password_button")
        ).performClick().performTextInput("test_password")

        rule.onNode(
            hasClickAction() and hasTestTag("Show password")
        ).performClick()

        rule.onNodeWithText("test_password").assertExists()
    }


}
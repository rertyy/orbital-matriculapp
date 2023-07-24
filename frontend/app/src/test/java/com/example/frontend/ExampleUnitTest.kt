package com.example.frontend

import com.example.frontend.ui.screens.auth.LoginViewModel
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun `loginSuccessful works`() {
        val test = LoginViewModel()
        test.loginSuccessful()
        assertEquals(true, test.loginSuccessful)
    }

    @Test
    fun `toggleLogin works`() {
        val test = LoginViewModel()
        test.toggleLogin()
        assertEquals(true, test.loginSuccessful)
    }

    @Test
    fun `triggerLoginError works`() {
        val test = LoginViewModel()
        test.triggerLoginError()
        assertEquals(true, test.loginError)
    }

    @Test
    fun `resetLoginError works`() {
        val test = LoginViewModel()
        test.triggerLoginError()
        test.resetLoginError()
        assertEquals(false, test.loginError)
    }
}
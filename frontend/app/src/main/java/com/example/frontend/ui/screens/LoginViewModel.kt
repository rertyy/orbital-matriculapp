package com.example.frontend.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.AuthApiService
import kotlinx.coroutines.launch

// TODO dependency injection to allow for API testing
// by separating the API service call from the view model
class LoginViewModel: ViewModel() {
    var username: String by mutableStateOf("")
        private set
    var password: String by mutableStateOf("")
        private set

    var loginSuccessful by mutableStateOf(false)
        private set
    var loginError by mutableStateOf(false)
        private set




    fun changeUsername(username: String) {
        this.username = username
    }

    fun changePassword(password: String) {
        this.password = password
    }

    fun loginSuccessful() {
        this.loginSuccessful = true
    }

    // for debug only
    fun toggleLogin() {
        this.loginSuccessful = !loginSuccessful
    }

    fun resetLoginError() {
        this.loginError = false
    }

    private fun triggerLoginError() {
        this.loginError = true
    }




    fun performLogin() {
        Log.d("Login", "Performing login")
        Log.d("LoginUsername", username)
        Log.d("LoginPassword", password)

        viewModelScope.launch {
            try {
                val request = LoginRequest(username, password)
                val loginResponse = AuthApiService.retrofitService.authenticateLogin(request)
                val authenticationResponse = loginResponse.body()
                if (loginResponse.isSuccessful) {
                    Log.d("Login", authenticationResponse.toString())
                    loginSuccessful()
                } else {
                    Log.e("Login", authenticationResponse.toString())
                    triggerLoginError()
                }
//        } catch (e: SocketTimeoutException) {
//            // Handle timeout exception
//            Log.e("Login", "Error: ${e.message}")
//        } catch (e: UnknownHostException) {
//            // Handle unknown host exception
//            Log.e("Login", "Error: ${e.message}")
//        } catch (e: IOException) {
//            // Handle general IO exception
//            Log.e("Login", "Error: ${e.message}")
//        } catch (e: HttpException) {
//            // Handle specific HTTP error codes
//            Log.e("Login", "Error: ${e.message}")
//        } catch (e: JsonParseException) {
//            // Handle JSON parsing exception
//            Log.e("Login", "Error: ${e.message}")
            } catch (e: Exception) {
                // Handle other exceptions
                Log.d("Login", "Error: ${e.message}")
                triggerLoginError()

            }
        }
    }
}




data class LoginRequest(
    val username: String,
    val password: String
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
)


package com.example.frontend.ui.screens

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import com.example.frontend.network.RestApiService
import kotlinx.coroutines.launch

// TODO dependency injection to allow for API testing
// by separating the API service call from the view model
class RegisterViewModel : UserInterfaceViewModel() {
    var email: String by mutableStateOf("")
        private set

    var registerSuccessful by mutableStateOf(false)
        private set
    var registerError by mutableStateOf(false)
        private set


    private fun triggerRegisterSuccessful() {
        this.registerSuccessful = true
    }

    private fun triggerRegisterError() {
        this.registerError = true
    }

    fun registerUser() {
        Log.d("Register", "Performing registration")
        Log.d("RegisterUsername", username)
        Log.d("RegisterPassword", password)
        Log.d("RegisterEmail", email)

        viewModelScope.launch {
            try {
                val request = RegisterRequest(username, password, email)
                val registerResponse = RestApiService.retrofitService.registerUser(request)
                val body = registerResponse.body()
                if (registerResponse.isSuccessful) {
                    Log.d("Register", body.toString())
                    triggerRegisterSuccessful()
                } else {
                    Log.e("Register", body.toString())
                    triggerRegisterError()
                }
            } catch (e: Exception) {
                // Handle other exceptions
                Log.d("Register", "Error: ${e.message}")
                triggerRegisterError()
            }

        }
    }

    fun performRegistration() {
        Log.d("Register", "Performing registration")
        Log.d("RegisterUsername", username)
        Log.d("RegisterPassword", password)

        viewModelScope.launch {
            try {
                val request = RegisterRequest(email, username, password)
                val registerResponse = RestApiService.retrofitService.registerUser(request)
                val authenticationResponse = registerResponse.body()
                if (registerResponse.isSuccessful) {
                    Log.d("Register", authenticationResponse.toString())
                    triggerRegisterSuccessful()
                } else {
                    Log.e("Register", authenticationResponse.toString())
                    triggerRegisterError()
                }
            } catch (e: Exception) {
                // Handle other exceptions
                Log.d("register", "Error: ${e.message}")
                triggerRegisterError()

            }
        }
    }

    fun resetRegisterError() {
        this.registerError = false
    }

    fun changeEmail(email: String) {
        this.email = email
    }


}


data class RegisterRequest(
    val username: String,
    val password: String,
    val email: String
)

package com.example.frontend.ui.screens.auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.frontend.repository.UserRepository
import dagger.Lazy
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

// TODO dependency injection to allow for API testing
// by separating the API service call from the view model

@HiltViewModel
class RegisterViewModel @Inject constructor(private val userRepository: Lazy<UserRepository>) :
    ViewModel() {
    init {
        userRepository.get()
    }

    var username: String by mutableStateOf("")
        private set
    var password: String by mutableStateOf("")
        private set

    fun changeUsername(username: String) {
        this.username = username
    }

    fun changePassword(password: String) {
        this.password = password
    }

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

    fun performRegistration() {
        Log.d("Register", "Performing registration")
        Log.d("RegisterUsername", username)
        Log.d("RegisterPassword", password)

        if (!checkValidRegistration()) {
            triggerRegisterError()
            return
        }

        viewModelScope.launch {
            try {
                val request = RegisterRequest(username, password, email)
                val registerResponse = userRepository.get().registerUser(request)
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

    private fun checkValidRegistration(): Boolean {
        val result =
            username.isNotEmpty() && password.isNotEmpty() //&& password.length > 8 && email.isNotEmpty() && "@" in email && "." in email
        Log.d("Register", "Valid registration: $result")
        return result
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

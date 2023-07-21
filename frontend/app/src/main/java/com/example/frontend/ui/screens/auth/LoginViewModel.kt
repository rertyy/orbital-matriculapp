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
class LoginViewModel @Inject constructor(private val userRepository: Lazy<UserRepository>) :
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


    var loginSuccessful by mutableStateOf(false)
        private set
    var loginError by mutableStateOf(false)
        private set

    internal fun loginSuccessful() {
        this.loginSuccessful = true
    }

    // for debug only
    fun toggleLogin() {
        this.loginSuccessful = !loginSuccessful
    }

    fun resetLoginError() {
        this.loginError = false
    }

    internal fun triggerLoginError() {
        this.loginError = true
    }

    private fun isValidLogin(): Boolean {
        val valid = username.isNotEmpty() && password.isNotEmpty()
        Log.d("Login", valid.toString())
        return valid
    }


    fun performLogin() {
        Log.d("Login", "Performing login")
        Log.d("Login", "login username is $username")
        Log.d("Login", "Login password is $password") //TODO remove this

        if (!isValidLogin()) {
            triggerLoginError()
            return
        }


        viewModelScope.launch {
            try {
                val request = LoginRequest(username, password)
                val loginResponse = userRepository.get().authenticateLogin(request)

//                val loginResponse = userRepository.authenticateLogin(request)
//                val loginResponse = RestApiService.retrofitService.authenticateLogin(request)

                // TODO sync up the header with the response
                val accessToken = loginResponse.headers()["Authorization"]
                Log.d("Login", "Access token is: $accessToken")
                userRepository.get().storeToken(accessToken.toString())
                val token = userRepository.get().getToken()
                Log.d("Login", "Stored access token is: $token")
                val authenticationResponse = loginResponse.body()
                if (loginResponse.isSuccessful) {
                    Log.d("Login", "success" + authenticationResponse.toString())
                    loginSuccessful()
                } else {
                    Log.e("Login", "error" + authenticationResponse.toString())
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


package com.example.frontend.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import com.example.frontend.network.AuthApiService
import com.example.frontend.ui.theme.FrontendTheme
import com.google.gson.JsonParseException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.time.Duration.Companion.seconds

@Composable
fun LoginScreen(onNavigate: () -> Unit) {
//    Button(onClick = onNavigate) {
//        Column {
//            Text(
//                text = stringResource(id = R.string.loginPage),
//                modifier = Modifier
//            )
//        }
//    }

    Login()
}

// TODO: login navigation, REST API, jwt, viewmodel and context, lock functions if user not logged in
@Composable
fun Login() {
    var username by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }

    var loginSuccessful by rememberSaveable { mutableStateOf(false) }
    var loginError by rememberSaveable { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()

    TestLogin(loginSuccessful, onChange = { loginSuccessful = it } )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = stringResource(id = R.string.loginPage),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.Start)
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField( // TODO add keyboard listener
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                performLogin(
                    LoginRequest(username, password),
                    { loginSuccessful = it },
                    { loginError = it})
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        // TODO change loginResponse
        if (loginSuccessful) {
            Text("Login successful", color = Color.Green)
        }
        if (loginError && !loginSuccessful) {
            Text("Login error", color = Color.Red)
            LaunchedEffect(Unit) {
                delay(3.seconds)
                loginError = false
            }

        }

    }
}


data class LoginRequest(
    val username: String?,
    val password: String?
)

data class LoginResponse(
    val success: Boolean,
    val message: String,
)


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FrontendTheme {
        Login()
    }

}




// TODO the scope is wrong, i think correct one is in the viewmodel?
fun performLogin(loginRequest: LoginRequest, loginSuccessful: (Boolean) -> Unit, loginError: (Boolean) -> Unit) {
    Log.d("Login", "Performing login")
    Log.d("LoginUsername", loginRequest.username.toString())
    Log.d("LoginPassword", loginRequest.password.toString())

    CoroutineScope(Dispatchers.Main).launch {
        try {
            val loginResponse = AuthApiService.retrofitService.authenticateLogin(loginRequest)
            val authenticationResponse = loginResponse.body()
            if (loginResponse.isSuccessful) {
                Log.d("Login", authenticationResponse.toString())
                loginSuccessful(true) // TODO this is definitely not secure
            } else {
                Log.e("Login", authenticationResponse.toString())
                loginError(true)
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
            loginError(true)

        }
    }
}

@Composable
fun TestLogin(
    checked: Boolean,
    onChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .size(20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = "Testing purpose only: enable login Successful:",
            fontSize = 10.sp )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}
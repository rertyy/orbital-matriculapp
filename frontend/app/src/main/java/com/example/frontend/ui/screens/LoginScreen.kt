package com.example.frontend.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.R
import com.example.frontend.ui.theme.FrontendTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds

@Composable
fun LoginScreen(onNavigateToRegister: () -> Unit) {
//    Button(onClick = onNavigate) {
//        Column {
//            Text(
//                text = stringResource(id = R.string.loginPage),
//                modifier = Modifier
//            )
//        }
//    }

    Login(onNavigateToRegister = onNavigateToRegister)
}

// TODO: login navigation, jwt, context, lock functions if user not logged in
@Composable
fun Login(loginViewModel: LoginViewModel = viewModel(), onNavigateToRegister: () -> Unit) {
    var passwordVisible by remember { mutableStateOf(false) }

    TestLogin(
        loginViewModel.loginSuccessful,
        onChange = { loginViewModel.toggleLogin() }
    )

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
            value = loginViewModel.username,
            onValueChange = { loginViewModel.changeUsername(it) },
            label = { Text(stringResource(id = R.string.username)) },
            placeholder = { Text(stringResource(id = R.string.username)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Next
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField( // TODO add keyboard listener
            value = loginViewModel.password,
            onValueChange = { loginViewModel.changePassword(it) },
            label = { Text(stringResource(id = R.string.password)) },
            placeholder = { Text(stringResource(id = R.string.password)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            trailingIcon = {
                val image =
                    if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff
                val description = if (passwordVisible) stringResource(R.string.hide_password)
                else stringResource(R.string.show_password)
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                loginViewModel.performLogin()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.login))
        }

        Button(
            onClick = {
                onNavigateToRegister()
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(stringResource(id = R.string.register))
        }

        // TODO change loginResponse
        if (loginViewModel.loginSuccessful) {
            Text("Login successful", color = Color.Green)
        }
        if (loginViewModel.loginError && !loginViewModel.loginSuccessful) {
            Text("Login error", color = Color.Red)
            LaunchedEffect(Unit) {
                delay(1.seconds) // TODO: check if this will delay if triggered multiple times
                loginViewModel.resetLoginError()
            }

        }

    }
}


@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    FrontendTheme {
        Login(onNavigateToRegister = { })
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
        Text(
            text = "Debug purpose only: enable login Successful:",
            fontSize = 10.sp
        )
        Switch(
            checked = checked,
            onCheckedChange = onChange,
            modifier = modifier
                .fillMaxWidth()
                .wrapContentWidth(Alignment.End)
        )
    }
}
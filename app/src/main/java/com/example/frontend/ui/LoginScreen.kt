package com.example.frontend.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.frontend.R

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

@Composable
fun Login() {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = username,
            onValueChange = { username = it },
            label = { Text(stringResource(id = R.string.username)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(id = R.string.username)) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )

        Button(
            onClick = {
                // Perform login logic here

                // If login is successful, navigate to the success screen
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
    }
}


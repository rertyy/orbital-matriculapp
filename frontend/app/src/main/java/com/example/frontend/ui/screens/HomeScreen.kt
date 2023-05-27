package com.example.frontend.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat.getDateTimeInstance

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CurrentTime()
    }

}



@Composable
fun CurrentTime() {
    var currentTime by rememberSaveable { mutableStateOf("")}
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    LaunchedEffect(true) {
        while (true) {
            val currentTimeString = getDateTimeInstance()
                .format(System.currentTimeMillis())

            currentTime = currentTimeString

            delay(1000) // Update every second

            // Use Dispatchers.Main.immediate to update the Composable in the main thread
            coroutineScope.launch(Dispatchers.Main.immediate) {
                // Update the Composable with the new state
                currentTime = currentTimeString
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text (
            text = stringResource(id = R.string.welcome),
            fontSize = 50.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = currentTime,
            fontSize = 30.sp,
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .background(
                    color = Color.Red,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.deadlines),
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(top = 5.dp,bottom = 5.dp)
                    .fillMaxWidth()
            )
        }

    }
}
//TODO unit test on this

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
fun CurrentTimePreview() {
    CurrentTime()
}
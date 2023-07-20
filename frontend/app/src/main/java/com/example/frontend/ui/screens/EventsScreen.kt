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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.DateFormat.getDateTimeInstance

@Composable
fun EventsScreen(
    navController: NavHostController,
    eventsViewModel: EventsViewModel,
    retryAction: () -> Unit
) {
    when (eventsViewModel.eventsUiState) {
        is EventsUiState.Loading -> EventsLoadingScreen()

        is EventsUiState.Error -> EventsErrorScreen(retryAction = retryAction)

        is EventsUiState.Success -> EventsSuccessScreen(
            navController = navController,
            eventsViewModel = eventsViewModel,
            retryAction = retryAction
        )
    }

}

@Composable
fun EventsLoadingScreen() {

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Text("Loading...")
    }

}

@Composable
fun EventsErrorScreen(
    retryAction: () -> Unit
) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(
            onClick = { retryAction() }
        ) {
            Text(stringResource(R.string.retry))
        }
    }

}

@Composable
fun EventsSuccessScreen(
    navController: NavHostController = rememberNavController(),
    eventsViewModel: EventsViewModel,
    retryAction: () -> Unit,

    ) {
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


// TODO: shift logic to viewmodel
@Composable
fun CurrentTime(eventsViewModel: EventsViewModel = viewModel()) {
    var currentTime by rememberSaveable { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val eventsList: List<Event> = eventsViewModel.events

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
        Text(
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

        // TODO separate to different function and hoist state
        Spacer(modifier = Modifier.height(20.dp))

        Deadlines(eventsViewModel)

        Spacer(modifier = Modifier.height(20.dp))
        Box(
            modifier = Modifier
                .background(
                    color = Color.Green,
                    shape = RoundedCornerShape(16.dp)
                )
        ) {
            Text(
                text = stringResource(id = R.string.events),
                fontSize = 30.sp,
                modifier = Modifier
                    .padding(top = 5.dp, bottom = 5.dp)
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


@Composable
fun Deadlines(eventsViewModel: EventsViewModel) {
    val eventList = eventsViewModel.events
    if (eventList == null) {
        Text("No events")
        return
    }
    Box(
        modifier = Modifier
            .background(
                color = Color.Red,
                shape = RoundedCornerShape(16.dp)
            )
    ) {
        Text(
            text = stringResource(id = R.string.urgent),
            fontSize = 30.sp,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 5.dp)
                .fillMaxWidth()
        )
        LazyColumn(
            modifier = Modifier
        ) {
            items(eventList) { event ->
                Text(event.eventName)
            }

        }
    }
}

@Composable
fun Reminders() {

}

@Composable
fun UpcomingEvents() {

}
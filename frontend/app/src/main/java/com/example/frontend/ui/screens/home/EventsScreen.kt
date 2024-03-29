package com.example.frontend.ui.screens.home

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Doorbell
import androidx.compose.material.icons.rounded.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.frontend.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import parseStringToDateTime
import java.text.DateFormat.getDateTimeInstance
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

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

    ) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        CurrentTime(eventsViewModel)
    }

}


// TODO: shift logic to viewmodel
@Composable
fun CurrentTime(eventsViewModel: EventsViewModel) {
    var currentTime by rememberSaveable { mutableStateOf("") }
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

        UpcomingEvents(eventsViewModel = eventsViewModel)

    }
}
//TODO unit test on this


@Composable
fun Deadlines(eventsViewModel: EventsViewModel) {
    val eventList = eventsViewModel.events

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.border(1.dp, Color.Red)) {
            Row(
                modifier = Modifier
                    .background(Color.Red)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.urgent),
                    color = Color.White, fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }

            LazyColumn(
                modifier = Modifier
            ) {
                items(eventList) { event ->
                    if (isUrgentEvent(
                            now = OffsetDateTime.now(),
                            event = parseStringToDateTime(event.eventStartDate)
                        )
                    ) {
                        EventDisplayBox(event = event, eventsViewModel = eventsViewModel)
                    } else {
                        Log.d("Deadlines from EventsScreen", "lmao")
                    }
                }

            }

        }


    }
}

@Composable
fun EventDisplayBox(event: Event, eventsViewModel: EventsViewModel) {

    var showEvent by remember { mutableStateOf(false) }

    val context: Context = LocalContext.current

    if (showEvent) {
        ViewEvent(event, { showEvent = false })
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
//            .border(1.dp, Color.Black)
//            .clip(RoundedCornerShape(30.dp))
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { showEvent = showEvent.not() }
            ) {
                Icon(imageVector = Icons.Rounded.Visibility, contentDescription = "show event")
            }

            Text(
                text = event.eventName,
                style = LocalTextStyle.current.copy(fontSize = 15.sp, fontWeight = FontWeight.Bold),
            )

            IconButton(
                onClick = { eventsViewModel.setReminder(event, context) },
            ) {
                Icon(imageVector = Icons.Rounded.Doorbell, contentDescription = "set reminder")
            }
        }

        Spacer(Modifier.height(5.dp))


    }
}

@Composable
fun ViewEvent(
    event: Event,
    onDismissAction: () -> Unit,
    backgroundColor: Color = Color(0xFFCCCCCC)
) {
    Dialog(onDismissRequest = onDismissAction) {
        Box(
            Modifier
                .clip(RectangleShape)
                .fillMaxWidth()
                .background(backgroundColor)
        ) {
            Column {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Blue)
                        .padding(start = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Event Name: ${event.eventName}",
                        color = Color.Green,
                        fontFamily = FontFamily.Monospace
                    )

                    Surface(
                        onClick = onDismissAction,
                        modifier = Modifier.padding(2.dp),
                        color = backgroundColor
                    ) {
                        Icon(Icons.Default.Close, contentDescription = "close")
                    }
                }



                Text("Event Description: ${event.eventBody}", fontFamily = FontFamily.Monospace)

                Text(
                    "Event Start Date: ${
                        timeToString(
                            time =
                            parseStringToDateTime(
                                event.eventStartDate
                            )
                        )
                    }",
                    fontFamily = FontFamily.Monospace
                )

                Text(
                    "Event End Date: ${timeToString(time = parseStringToDateTime(event.eventEndDate))}",
                    fontFamily = FontFamily.Monospace
                )


            }
//            Spacer(modifier = Modifier.height(30.dp))
//
//            Surface(
//                modifier = Modifier.align(Alignment.Center),
//                onClick = onDismissAction,
//                shape = RectangleShape,
//                color = backgroundColor,
//                border = BorderStroke(Dp.Hairline, Color.Black)
//            ) {
//                Text(
//                    "Close",
//                    fontFamily = FontFamily.Monospace,
//                    modifier = Modifier
//                        .widthIn(120.dp)
//                        .padding(vertical = 8.dp),
//                    textAlign = TextAlign.Center
//                )
//            }
        }

    }
}

@Composable
fun timeToString(time: OffsetDateTime): String {
    val localdatetime = time.toLocalDateTime()

    val datetimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    return localdatetime.format(datetimeFormatter)
}

@Composable
fun isUrgentEvent(now: OffsetDateTime = OffsetDateTime.now(), event: OffsetDateTime): Boolean {
    Log.d("isUrgentEvent", "now: $now, event: $event")
//    return false
    if (event < now) {
        return true
    }
    return now.until(event, ChronoUnit.DAYS) < 7
}

@Composable
fun UpcomingEvents(eventsViewModel: EventsViewModel) {

    val eventList = eventsViewModel.events

    Box(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.border(1.dp, Color.Green)) {
            Row(
                modifier = Modifier
                    .background(Color.Green)
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(id = R.string.events),
                    color = Color.White, fontFamily = FontFamily.Serif,
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 5.dp)
                        .fillMaxWidth()
                        .align(Alignment.CenterVertically)
                )
            }

            LazyColumn(
                modifier = Modifier
            ) {
                items(eventList) { event ->
                    if (!isUrgentEvent(
                            now = OffsetDateTime.now(),
                            event = parseStringToDateTime(event.eventStartDate)
                        )
                    ) {
                        EventDisplayBox(event = event, eventsViewModel = eventsViewModel)
                    } else {
                        //
                    }
                }

            }
        }
    }

}
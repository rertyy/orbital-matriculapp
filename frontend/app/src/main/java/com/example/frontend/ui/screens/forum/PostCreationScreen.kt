package com.example.frontend.ui.screens.forum

import Thread
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.frontend.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PostCreation(onBack: () -> Unit) {
    val forumViewModel = hiltViewModel<ForumViewModel>()


    var isExpanded: Boolean by remember { mutableStateOf(false) }
    var category: String by remember { mutableStateOf("") }
    var title: String by remember { mutableStateOf("") }
    var body: String by remember { mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        Spacer(modifier = Modifier.height(10.dp))

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            TextButton(
                onClick = {
                    onBack()
                },
                //modifier = Modifier.align(Alignment.Start)
            ) {
                Text(
                    text = stringResource(id = R.string.back),
                    color = Color.Blue
                )
            }

            Button(
                onClick = {
                    val newThread = Thread(title = title, body = body)
                    forumViewModel.addThread(newThread)
                    onBack()
                },
                //modifier = Modifier.align(Alignment.End)
            ) {
                Column {
                    Text(
                        text = stringResource(id = R.string.createPost),
                        modifier = Modifier.padding(5.dp),
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }


        }


        Spacer(Modifier.height(25.dp))

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = it },
            modifier = Modifier
                .align(Alignment.Start)
                .clip(RoundedCornerShape(10.dp))

        ) {
            TextField(
                value = category,
                onValueChange = { category = it },
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor(),
                placeholder = { Text("Select Category") }
            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false },
            ) {

                DropdownMenuItem(
                    text = {
                        Text("Academics")
                    }, onClick = {
                        category = "Academics"
                        isExpanded = false
                    })

                DropdownMenuItem(
                    text = {
                        Text("Social")
                    }, onClick = {
                        category = "Social"
                        isExpanded = false
                    })

            }

        }

        Spacer(Modifier.height(20.dp))

        EditTitle(
            value = title,
            onValueChange = { title = it }
        )

        Spacer(Modifier.height(30.dp))

        EditBody(
            value = body,
            onValueChange = { body = it }
        )

    }

}

@Composable
fun EditTitle(value: String, onValueChange: (String) -> Unit) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = R.string.title)) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun EditBody(value: String, onValueChange: (String) -> Unit) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = R.string.body)) },
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        textStyle = MaterialTheme.typography.bodySmall
    )
}

/*TODO  Add AlertDialog for empty body/title, add back button */



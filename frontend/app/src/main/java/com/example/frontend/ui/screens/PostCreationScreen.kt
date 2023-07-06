package com.example.frontend.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.frontend.R
import com.example.frontend.ui.theme.FrontendTheme
import kotlinx.coroutines.delay
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun postCreation(onCreatePost: () -> Unit) {

    var isExpanded: Boolean by remember {mutableStateOf(false)}
    var category: String by remember {mutableStateOf("Select Category")}
    var title: String by remember{ mutableStateOf("") }
    var body: String by remember{ mutableStateOf("") }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Card(modifier = Modifier.align(Alignment.End)) {
            Column {
                Text(
                    text = stringResource(id = R.string.createPost),
                    modifier = Modifier.padding(5.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
        }

        Spacer(Modifier.height(25.dp))

        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = {isExpanded = it},
            modifier = Modifier
                .align(Alignment.Start)
                .size(width = 200.dp, height = 25.dp)
                .clip(RoundedCornerShape(10.dp))

        ) {
            TextField(
                value = category,
                onValueChange = {},
                readOnly = true,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
                },
                colors = ExposedDropdownMenuDefaults.textFieldColors(),
                modifier = Modifier.menuAnchor()

            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false}
            ) {
                DropdownMenuItem(
                    text = {
                        Text("Academics")
                           }
                    , onClick = {
                        category = "Academics"
                        isExpanded = false
                    })

                DropdownMenuItem(
                    text = {
                        Text("Social")
                    }
                    , onClick = {
                        category = "Social"
                        isExpanded = false
                    })
                
            }

        }

        Spacer(Modifier.height(20.dp))
        
        editTitle(
            value = title,
            onValueChange = { title = it }
        )

        Spacer(Modifier.height(30.dp))

        editBody(
            value = body,
            onValueChange = {body = it}
        )

    }

}

@Composable
fun editTitle(value: String, onValueChange: (String) -> Unit) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = R.string.title)) },
        modifier = Modifier.fillMaxWidth(),
        textStyle = MaterialTheme.typography.bodySmall
    )
}

@Composable
fun editBody(value: String, onValueChange: (String) -> Unit) {

    TextField(
        value = value,
        onValueChange = onValueChange,
        label = {Text(stringResource(id = R.string.body))},
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        textStyle = MaterialTheme.typography.bodySmall
    )
}

/*TODO  Add AlertDialog for empty body/title, add back button */



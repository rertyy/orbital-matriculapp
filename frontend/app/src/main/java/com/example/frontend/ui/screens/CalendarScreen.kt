package com.example.frontend.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController

@Composable
fun CalendarScreen(navController: NavHostController) {
    MyContent()

}


@Composable
fun MyContent() {
    val mAnnotatedLinkString = buildAnnotatedString {

        val str = "NUS Academic Calendar AY23/24"
        val startIndex = 0
        val endIndex = str.length - 1
        append(str)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline
            ), start = startIndex, end = endIndex
        )

        addStringAnnotation(
            tag = "URL",
            annotation = "https://www.nus.edu.sg/nusbulletin/ay202324/general-information/academic-calendar/",
            start = startIndex,
            end = endIndex
        )
    }

    // UriHandler parse and opens URI inside
    // AnnotatedString Item in Browse
    val mUriHandler = LocalUriHandler.current

    Column(
        Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        // ???? Clickable text returns position of text
        // that is clicked in onClick callback
        ClickableText(
            text = mAnnotatedLinkString,
            onClick = {
                mAnnotatedLinkString
                    .getStringAnnotations("URL", it, it)
                    .firstOrNull()?.let { stringAnnotation ->
                        mUriHandler.openUri(stringAnnotation.item)
                    }
            }
        )
    }
}

// For displaying preview in
// the Android Studio IDE emulator
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyContent()
}
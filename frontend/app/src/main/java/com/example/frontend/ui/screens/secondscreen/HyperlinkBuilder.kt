package com.example.frontend.ui.screens.secondscreen

import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp

@Composable
fun HyperlinkText(text: String, link: String? = null, modifier: Modifier = Modifier) {
    if (link.isNullOrEmpty()) {
        Text(text = text, fontSize = 16.sp, modifier = modifier)
        return
    }

    val mAnnotatedLinkString = buildAnnotatedString {
        val startIndex = 0
        val endIndex = text.length
        append(text)
        addStyle(
            style = SpanStyle(
                color = Color.Blue,
                textDecoration = TextDecoration.Underline,
                fontSize = 16.sp
            ), start = startIndex, end = endIndex
        )

        addStringAnnotation(
            tag = "URL",
            annotation = link,
            start = startIndex,
            end = endIndex
        )
    }
    // UriHandler parse and opens URI inside
    // AnnotatedString Item in Browse
    val mUriHandler = LocalUriHandler.current
    ClickableText(
        text = mAnnotatedLinkString,
        onClick = {
            mAnnotatedLinkString
                .getStringAnnotations("URL", it, it)
                .firstOrNull()?.let { stringAnnotation ->
                    mUriHandler.openUri(stringAnnotation.item)
                }
        },
        modifier = modifier
    )

}
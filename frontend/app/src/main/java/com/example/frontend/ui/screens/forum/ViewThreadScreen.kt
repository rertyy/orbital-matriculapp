package com.example.frontend.ui.screens.forum

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TextField
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
import com.example.frontend.R

@Composable
fun ViewThread(
    forumViewModel: ForumViewModel,
    onBack: () -> Unit,
    forumUiState: ForumUiState
) {
    val scrollState = rememberScrollState()
    var commentBody: String by remember { mutableStateOf("") }
    var thread: Thread by remember { mutableStateOf(defaultThread) }
    var replies: List<Reply> by remember { mutableStateOf(listOf(defaultReply)) }

    when (forumUiState) {
        is ForumUiState.GetReplies -> {
            thread = forumUiState.thread
            replies = forumUiState.replies
        }

        else -> {}
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .scrollable(state = scrollState, orientation = Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        TextButton(
            onClick = { onBack() }
        ) {
            Text(
                text = stringResource(id = R.string.back),
                color = Color.Blue
            )
        }


        Spacer(Modifier.height(25.dp))

        TextField(
            value = thread.title,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(25.dp))

        TextField(
            value = thread.body,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        )

        Spacer(Modifier.height(10.dp))

        ReplyList(listReplies = replies)

        Spacer(Modifier.height(10.dp))

        Text(text = stringResource(id = R.string.comment))
        Divider(color = Color.Red, thickness = 1.dp)

        Comment(
            value = commentBody,
            onValueChange = { commentBody = it }
        )

        Button(
            onClick = {
                val newComment = Reply(
                    body = commentBody,
                    threadId = thread.threadId
                )

                forumViewModel.addReply(newComment)

                commentBody = ""
            }
        ) {

        }

    }
}


@Composable
fun ReplyList(listReplies: List<Reply>) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {
        items(listReplies) { post ->
            TextField(
                value = post.body,
                onValueChange = {},
                readOnly = true
            )
        }
    }
}


@Composable
fun Comment(value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(stringResource(id = R.string.body)) },
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(10.dp))
    )
}


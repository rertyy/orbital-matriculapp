package com.example.frontend.ui.screens.forum

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.frontend.ForumNavGraph
import com.example.frontend.R

@Composable
fun ForumScreen(
    forumUiState: ForumUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier,
    onCreateThread: () -> Unit,
    navController: NavController,
    forumViewModel: ForumViewModel,
) {
    when (forumUiState) {
        is ForumUiState.Loading -> LoadingScreen(modifier, onCreateThread)
        is ForumUiState.Success -> ResultScreen(
            forumUiState.threadList,
            modifier,
            onCreateThread,
            navController,
            forumViewModel
        )

        is ForumUiState.Error -> ErrorScreen(retryAction, modifier, onCreateThread)
        is ForumUiState.GetReplies -> {
            Log.d("ForumUiState Changed", "here")
            ViewThread(
                forumViewModel = forumViewModel,
                onBack = { navController.navigate(ForumNavGraph.Posts.route) },
                forumUiState = forumUiState
            )
        }

    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier, onCreatePost: () -> Unit) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
//        Image(
//            modifier = Modifier.size(200.dp),
//            // TODO make the image
//            painter = painterResource(R.drawable.loading_img),
//            contentDescription = stringResource(R.string.loading)
//        )
        Text("Loading...")
    }
}

@Composable
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier, onCreatePost: () -> Unit) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(stringResource(R.string.loading_failed))
        Button(onClick = retryAction) {
            Text(stringResource(R.string.retry))
        }
    }
}

@Composable
fun ResultScreen(
    threads: List<Thread>,
    modifier: Modifier = Modifier,
    onCreatePost: () -> Unit,
    navController: NavController,
    forumViewModel: ForumViewModel
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Log.d("GET posts", threads.toString())
        ThreadsList(
            threads = threads,
            onCreateThread = onCreatePost,
            navController = navController,
            forumViewModel = forumViewModel
        )
    }
}


// TODO: figure out how to do @PreviewComposable with List<>
//@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun ThreadsList(
    @PreviewParameter(MultiPostProvider::class) threads: List<Thread>,
    modifier: Modifier = Modifier,
    forumViewModel: ForumViewModel,
    onCreateThread: () -> Unit,
    navController: NavController
) {
    Log.d("posts", threads.toString())
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            // TODO change to add post
            onClick = { onCreateThread() },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.addNewPost))
        }
        ThreadCard(
            thread = thread1,
            forumViewModel = forumViewModel,
            navController = navController,
            showDelete = false
        )
        ThreadCard(
            thread = thread2,
            forumViewModel = forumViewModel,
            navController = navController,
            showDelete = false
        )

        LazyColumn(modifier = modifier) {

            items(threads) { post ->
                ThreadCard(
                    thread = post,
                    navController = navController,
                    forumViewModel = forumViewModel
                )
            }
        }
    }
}


//@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun ThreadCard(
    @PreviewParameter(SinglePostProvider::class) thread: Thread,
    forumViewModel: ForumViewModel,
    navController: NavController,
    showDelete: Boolean = true
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Row(
            modifier = Modifier
                .align(Alignment.Start)
                .padding(8.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = thread.title,
                style = LocalTextStyle.current.copy(fontSize = 15.sp, fontWeight = Bold),
            )


            Row {
//                IconButton(
//                    onClick = { forumViewModel.modifyThread(thread, thread2) },
//                    modifier = Modifier
//                ) {
//                    Icon(imageVector = Icons.Rounded.Edit, contentDescription = "edit post")
//                }
                if (showDelete) {
                    IconButton(
                        onClick = { forumViewModel.deleteThread(thread.threadId) }
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Delete,
                            contentDescription = "delete post",
                            tint = Color.Red
                        )
                    }
                }


            }

        }
//            Text(
//                text = post.title,
//                style = LocalTextStyle.current.copy(fontSize = 15.sp, fontWeight = Bold),
//            )
        Text(text = thread.body, style = LocalTextStyle.current.copy(fontSize = 12.sp))

        Button(
            onClick = {
                forumViewModel.getReplies(thread.threadId)
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(id = R.string.comment))
        }

    }
}

val thread1: Thread = Thread(
    1,
    "Hello everyone!",
    "I'm new to this forum. I'm looking forward to meeting you all!"
)

//"cat-name2",
//1,
//"User2",
//    OffsetDateTime.now().toOffsetTime()
//    OffsetDateTime.now(),


val thread2 = Thread(2, "Courses", "May I know what courses I have to take this sem?")

//"cat-name2",
//1,
//"User1",
//    OffsetDateTime.now(),
//    OffsetDateTime.now(),


private class MultiPostProvider : PreviewParameterProvider<Thread> {
    override val values: Sequence<Thread>
        get() = sequenceOf(
            thread1,
            thread2
        )
}

private class SinglePostProvider : PreviewParameterProvider<Thread> {
    override val values: Sequence<Thread>
        get() = sequenceOf(
            thread1
        )
}

package com.example.frontend.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.frontend.R

@Composable
fun ForumScreen(
    postsUiState: PostsUiState,
    retryAction: () -> Unit,
    modifier: Modifier = Modifier
) {
    when (postsUiState) {
        is PostsUiState.Loading -> LoadingScreen(modifier)
        is PostsUiState.Success -> ResultScreen(postsUiState.posts, modifier)
        is PostsUiState.Error -> ErrorScreen(retryAction, modifier)
    }
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
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
fun ErrorScreen(retryAction: () -> Unit, modifier: Modifier = Modifier) {
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
fun ResultScreen(posts: List<Post>, modifier: Modifier = Modifier) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier.fillMaxSize()
    ) {
        Log.d("GET posts", posts.toString())
        PostsList(posts = posts)
    }
}


// TODO: figure out how to do @PreviewComposable with List<>
//@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun PostsList(
    @PreviewParameter(MultiPostProvider::class) posts: List<Post>,
    modifier: Modifier = Modifier,
    postsViewModel: PostsViewModel = viewModel(),
) {
    Log.d("posts", posts.toString())
    Column(modifier = Modifier.fillMaxWidth()) {
        Button(
            // TODO change to add post
            onClick = { postsViewModel.addPost(post1) },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(stringResource(R.string.addNewPost))
        }

        LazyColumn(modifier = modifier) {
            items(posts) { post ->
                PostCard(post = post)
            }
        }
    }
}


@Preview(backgroundColor = 0xFFFFFF, showBackground = true)
@Composable
fun PostCard(@PreviewParameter(SinglePostProvider::class) post: Post) {
    Card(
        modifier = Modifier.padding(8.dp),
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Text(
                text = post.title,
                style = LocalTextStyle.current.copy(fontSize = 15.sp, fontWeight = Bold)
            )
            Text(text = post.body, style = LocalTextStyle.current.copy(fontSize = 12.sp))
            Text(text = post.body, style = LocalTextStyle.current.copy(fontSize = 5.sp))
        }
    }
}


val post1 = Post(
    "Hello World!",
    "consectetur.",
    1,
    "cat-name2",
    1,
    "User2",
//    OffsetDateTime.now().toOffsetTime()
//    OffsetDateTime.now(),
)

val post2 = Post(
    "Hello!",
    "consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    1,
    "cat-name2",
    1,
    "User1",
//    OffsetDateTime.now(),
//    OffsetDateTime.now(),
)

private class MultiPostProvider : PreviewParameterProvider<Post> {
    override val values: Sequence<Post>
        get() = sequenceOf(
            post1,
            post2
        )
}

private class SinglePostProvider : PreviewParameterProvider<Post> {
    override val values: Sequence<Post>
        get() = sequenceOf(
            post1
        )
}

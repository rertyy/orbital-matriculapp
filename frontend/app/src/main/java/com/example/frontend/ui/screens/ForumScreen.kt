package com.example.frontend.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
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
        // TODO serialise this to read the structs
        PostsList(posts = posts)
    }
}


// TODO: figure out how to do @PreviewComposable with List<>
@Composable
fun PostsList(
    posts: List<Post>,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier) {
        items(posts) { post ->
            Text(
                post.title,
                modifier = Modifier.padding(8.dp)
            )
            Spacer(modifier = modifier.height(25.dp))
        }
    }
}


val b = listOf(
    Post("title1", "body1", "cat-name", "name", "date", "time"),
    Post("title2", "body2", "cat-name2", "name2", "date", "time")
)

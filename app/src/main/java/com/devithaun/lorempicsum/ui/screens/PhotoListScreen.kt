package com.devithaun.lorempicsum.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.devithaun.domain.model.Photo
import com.devithaun.lorempicsum.ui.components.ErrorDialog
import com.devithaun.lorempicsum.ui.components.FilterDropDown
import com.devithaun.lorempicsum.ui.components.LoadingSpinner
import com.devithaun.lorempicsum.viewmodel.PhotosViewModel

@Composable
fun PhotoListScreen(viewModel: PhotosViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val authors = remember(uiState) {
        (uiState as? PhotosViewModel.UiState.Success)?.photos?.map { it.author }?.distinct()
            ?: emptyList()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Column {
            when (uiState) {
                is PhotosViewModel.UiState.Loading -> {
                    LoadingSpinner()
                }

                is PhotosViewModel.UiState.Success -> {
                    FilterDropDown(authors, filter, onAuthorSelected = {
                        it?.let { viewModel.setFilter(it) } ?: viewModel.clearFilter()
                    })
                    PhotoList((uiState as PhotosViewModel.UiState.Success).photos)
                }

                is PhotosViewModel.UiState.Error -> {
                    ErrorDialog(
                        title = "Something went wrong",
                        message = (uiState as PhotosViewModel.UiState.Error).message,
                        onRetry = { viewModel.retry() }
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoList(photos: List<Photo>) {
    LazyColumn {
        items(photos) { photo ->
            PhotoItem(photo = photo)
        }
    }
}

@Composable
fun PhotoItem(photo: Photo) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        )
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    .data(photo.url)
                    .crossfade(true)
                    .diskCachePolicy(CachePolicy.ENABLED)
                    .memoryCachePolicy(CachePolicy.ENABLED)
                    .build(),
                contentDescription = "Photo by ${photo.author}",
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f),
                contentScale = ContentScale.Crop
            )

            Text(
                text = photo.author,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
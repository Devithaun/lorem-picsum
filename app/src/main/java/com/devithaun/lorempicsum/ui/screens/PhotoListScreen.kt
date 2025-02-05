package com.devithaun.lorempicsum.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devithaun.domain.model.Photo
import com.devithaun.lorempicsum.ui.components.ErrorDialog
import com.devithaun.lorempicsum.ui.components.FilterDropDown
import com.devithaun.lorempicsum.ui.components.LoadingSpinner
import com.devithaun.lorempicsum.viewmodel.PhotosViewModel

@Composable
fun PhotoListScreen(viewModel: PhotosViewModel) {
    val photos by viewModel.photos.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val authors = photos.map { it.author }.distinct()
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val retrying by viewModel.isRetrying.collectAsState()

    Column {
        FilterDropDown(authors, filter, onAuthorSelected = {
            it?.let { viewModel.setFilter(it) } ?: viewModel.clearFilter()
        })

        if (loading) {
            LoadingSpinner()
        } else {
            if (error.isNullOrBlank()) {
                PhotoList(photos = photos, filter = filter)
            } else {
                ErrorDialog(
                    "Something went wrong",
                    error!!,
                    isLoading = retrying,
                    onRetry = { viewModel.retry() })
            }
        }
    }
}

@Composable
fun PhotoList(photos: List<Photo>, filter: String?) {
    val filteredPhotos = if (!filter.isNullOrEmpty()) {
        photos.filter { it.author.contains(filter, ignoreCase = true) }
    } else {
        photos
    }

    LazyColumn {
        items(filteredPhotos) { photo ->
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
                model = photo.url,
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
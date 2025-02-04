package com.devithaun.lorempicsum.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devithaun.domain.model.Photo
import com.devithaun.lorempicsum.viewmodel.PhotosViewModel

@Composable
fun PhotoListScreen(viewModel: PhotosViewModel) {
    val photos by viewModel.photos.collectAsState()
    val filter by viewModel.filter.collectAsState()

    Column {
        PhotoList(photos = photos, filter = filter)
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
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier.padding(8.dp)
        ) {
            AsyncImage(
                model = photo.url,
                contentDescription = "Photo",
                modifier = Modifier.size(100.dp)
            )
            Column(modifier = Modifier.padding(start = 8.dp)) {
                Text(
                    text = "Author: ${photo.author}",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }
    }
}




package com.devithaun.lorempicsum.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.devithaun.domain.model.Photo
import com.devithaun.lorempicsum.viewmodel.PhotosViewModel

@Composable
fun PhotoListScreen(viewModel: PhotosViewModel) {
    val photos by viewModel.photos.collectAsState()
    val filter by viewModel.filter.collectAsState()
    val authors = photos.map { it.author }.distinct()

    Column {
        FilterDropDown(authors, filter, onAuthorSelected = {
            it?.let { viewModel.setFilter(it) } ?: viewModel.clearFilter()
        })
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

@Composable
fun FilterDropDown(
    authors: List<String>,
    filter: String?,
    onAuthorSelected: (String?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
            .clickable { expanded = true }
            .padding(12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = filter.takeUnless { it.isNullOrEmpty() } ?: "Select an Author",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            DropdownMenuItem(
                text = { Text("Show All") },
                onClick = {
                    onAuthorSelected(null)
                    expanded = false
                }
            )
            authors.forEach { author ->
                DropdownMenuItem(
                    text = { Text(author) },
                    onClick = {
                        onAuthorSelected(author)
                        expanded = false
                    }
                )
            }
        }
    }
}
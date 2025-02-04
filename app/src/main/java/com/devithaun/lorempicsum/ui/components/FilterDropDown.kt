package com.devithaun.lorempicsum.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

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
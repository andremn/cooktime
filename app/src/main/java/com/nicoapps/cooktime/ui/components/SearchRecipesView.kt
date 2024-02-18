package com.nicoapps.cooktime.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchRecipesView(
    modifier: Modifier = Modifier,
    onSearchTextChanged: (String) -> Unit,
    onIsActiveChanged: (Boolean) -> Unit
) {
    repeat(15) { index ->
        val resultText = "Recipe #${index + 1}"

        ListItem(
            headlineContent = { Text(resultText) },
            supportingContent = { Text("Additional info") },
            leadingContent = {
                Icon(
                    Icons.Filled.Star,
                    contentDescription = null
                )
            },
            modifier = modifier
                .clickable {
                    onSearchTextChanged(resultText)
                    onIsActiveChanged(false)
                }
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}
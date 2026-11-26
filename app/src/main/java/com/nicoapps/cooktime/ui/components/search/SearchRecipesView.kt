package com.nicoapps.cooktime.ui.components.search

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.nicoapps.cooktime.model.Recipe
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun SearchRecipesView(
    modifier: Modifier = Modifier,
    recipes: List<Recipe>,
    onRecipeSelected: (Recipe) -> Unit
) {
    val dateFormatter = remember { DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT) }

    // The full-screen search bar leaves the keyboard inset to its content, so without the IME
    // padding the last results would sit behind the keyboard, out of reach.
    LazyColumn(modifier = modifier.fillMaxWidth().imePadding()) {
        items(items = recipes, key = { recipe -> recipe.id }) { recipe ->
            ListItem(
                onClick = { onRecipeSelected(recipe) },
                supportingContent = {
                    Text(text = recipe.createdAt.format(dateFormatter))
                },
                leadingContent = {
                    if (recipe.isStarred) {
                        Icon(
                            Icons.Filled.Star,
                            contentDescription = null
                        )
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 4.dp)
            ) {
                Text(
                    text = recipe.name
                )
            }
        }
    }
}

package com.nicoapps.cooktime.ui.components.search

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
import com.nicoapps.cooktime.model.Recipe
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Composable
fun SearchRecipesView(
    modifier: Modifier = Modifier,
    recipes: List<Recipe>,
    onIsActiveChanged: (Boolean) -> Unit,
    onRecipeSelected: (Recipe) -> Unit
) {
    recipes.forEach { recipe ->
        ListItem(
            headlineContent = {
                Text(
                    text = recipe.name
                )
            },
            supportingContent = {
                Text(
                    text = recipe.createdAt.format(
                        DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                    )
                )
            },
            leadingContent = {
                if (recipe.isStarred) {
                    Icon(
                        Icons.Filled.Star,
                        contentDescription = null
                    )
                }
            },
            modifier = modifier
                .clickable {
                    onIsActiveChanged(false)
                    onRecipeSelected(recipe)
                }
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp)
        )
    }
}
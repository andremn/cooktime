package com.nicoapps.cooktime.ui.components.recipe

import androidx.compose.animation.Crossfade
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.colorResource
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.defaultAnimationSpec

@Composable
fun StarredRecipeIcon(
    isStarred: Boolean
) {
    Crossfade(
        targetState = isStarred,
        label = "recipeIsStarredIconAnimation",
        animationSpec = defaultAnimationSpec()
    ) { starred ->
        if (starred) {
            Icon(
                imageVector = Icons.Default.Favorite,
                tint = colorResource(id = R.color.starred_recipe),
                contentDescription = "Localized description",
            )
        } else {

            Icon(
                imageVector = Icons.Default.FavoriteBorder,
                contentDescription = "Localized description",
            )
        }
    }
}
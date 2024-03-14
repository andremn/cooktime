package com.nicoapps.cooktime.ui.screens.recipe.edit.appbar

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.defaultEnterAnimation
import com.nicoapps.cooktime.ui.defaultExitAnimation

@Composable
fun ViewRecipeAppBottomBarFab(
    modifier: Modifier = Modifier,
    isVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        modifier = modifier,
        visible = isVisible,
        enter = defaultEnterAnimation(),
        exit = defaultExitAnimation()
    ) {
        ExtendedFloatingActionButton(
            onClick = { onClick() },
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation(),
            text = {
                Text(
                    text = stringResource(id = R.string.start_recipe_button_text)
                )
            },
            icon = {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Localized description"
                )
            }
        )
    }
}
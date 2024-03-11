package com.nicoapps.cooktime.ui

import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import com.nicoapps.cooktime.R

fun<T> defaultAnimationSpec() = tween<T>(
    durationMillis = 350
)

@Composable
fun dialogSurfaceColor() =
    MaterialTheme.colorScheme.surfaceColorAtElevation(
        dimensionResource(id = R.dimen.dialog_elevation)
    )

@Composable
fun dialogTextFieldColors() =
    TextFieldDefaults.colors(
        focusedContainerColor = dialogSurfaceColor(),
        unfocusedContainerColor = dialogSurfaceColor(),
        errorContainerColor = dialogSurfaceColor()
    )
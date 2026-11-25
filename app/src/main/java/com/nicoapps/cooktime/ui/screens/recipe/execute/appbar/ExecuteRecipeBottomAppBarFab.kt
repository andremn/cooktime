package com.nicoapps.cooktime.ui.screens.recipe.execute.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.nicoapps.cooktime.R

@Composable
fun ExecuteRecipeBottomAppBarFab(
    onClick: () -> Unit
) {
    FloatingActionButton(
        onClick = { onClick() },
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
    ) {
        Icon(
            imageVector = Icons.Default.Done,
            contentDescription = stringResource(R.string.cd_finish_recipe)
        )
    }
}
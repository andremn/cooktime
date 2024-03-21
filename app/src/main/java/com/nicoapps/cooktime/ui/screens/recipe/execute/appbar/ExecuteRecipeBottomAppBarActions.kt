package com.nicoapps.cooktime.ui.screens.recipe.execute.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import com.nicoapps.cooktime.R

@Composable
fun ExecuteRecipeBottomAppBarActions(
    onResetClick: () -> Unit,
    onAlarmClick: () -> Unit
) {
    IconButton(
        onClick = { onResetClick() }
    ) {
        Icon(
            imageVector = Icons.Default.Refresh,
            contentDescription = "Localized description"
        )
    }

    IconButton(
        onClick = { onAlarmClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_timer_24),
            contentDescription = "Localized description"
        )
    }
}
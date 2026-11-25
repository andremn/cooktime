package com.nicoapps.cooktime.ui.screens.recipe.execute.appbar

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
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
            contentDescription = stringResource(R.string.cd_uncheck_all)
        )
    }

    IconButton(
        onClick = { onAlarmClick() }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.baseline_timer_24),
            contentDescription = stringResource(R.string.cd_timer)
        )
    }
}
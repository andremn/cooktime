package com.nicoapps.cooktime.ui.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.AppNavGraphTopBarContentType
import com.nicoapps.cooktime.ui.AppNavGraphTopBarState

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    onComposing: (AppNavGraphState) -> Unit
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onComposing(
            AppNavGraphState(
                topBar = AppNavGraphTopBarState(
                    contentType = AppNavGraphTopBarContentType.TITLE_ONLY,
                    title = {
                        Text(
                            text = context.resources.getString(R.string.settings_title)
                        )
                    }
                )
            )
        )
    }

    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.settings_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = stringResource(R.string.settings_placeholder_description),
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
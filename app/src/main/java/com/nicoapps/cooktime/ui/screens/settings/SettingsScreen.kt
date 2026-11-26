package com.nicoapps.cooktime.ui.screens.settings

import androidx.activity.compose.LocalOnBackPressedDispatcherOwner
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.nicoapps.cooktime.R
import com.nicoapps.cooktime.ui.components.AppTitleTopBar
import com.nicoapps.cooktime.ui.transitions.screenContentTransition
import com.nicoapps.cooktime.ui.transitions.topBarTransition

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {
    val onBackPressedDispatcher =
        checkNotNull(LocalOnBackPressedDispatcherOwner.current).onBackPressedDispatcher

    Scaffold(
        modifier = modifier,
        containerColor = Color.Transparent,
        topBar = {
            AppTitleTopBar(
                modifier = Modifier.topBarTransition(),
                title = {
                    Text(text = stringResource(R.string.settings_title))
                },
                onNavigationIconClick = { onBackPressedDispatcher.onBackPressed() }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .screenContentTransition()
                .fillMaxSize()
                .padding(paddingValues),
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
}

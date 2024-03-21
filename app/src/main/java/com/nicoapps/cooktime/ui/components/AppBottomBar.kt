package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.defaultEnterAnimation
import com.nicoapps.cooktime.ui.defaultExitAnimation
import kotlinx.coroutines.delay

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    appNavGraphState: AppNavGraphState
) {
    if (appNavGraphState.bottomBar.visible) {
        var isReady by remember { mutableStateOf(false) }

        LaunchedEffect(Unit) {
            delay(appNavGraphState.bottomBar.delayToBecomeVisible)
            isReady = true
        }

        AnimatedVisibility(
            modifier = modifier
                .windowInsetsPadding(BottomAppBarDefaults.windowInsets),
            visible = isReady,
            label = "bottomAppBarAnimation",
            enter = defaultEnterAnimation(),
            exit = defaultExitAnimation()
        ) {
            BottomAppBar(
                modifier = modifier
                    .animateContentSize(),
                actions = {
                    AppBottomBarActionsContainer(appNavGraphState.bottomBar.actions)
                },
                floatingActionButton = {
                    AppBottomBarFabContainer(
                        appNavGraphState.bottomBar.floatingActionButton
                    )
                }
            )
        }
    }
}
package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.defaultEnterAnimation
import com.nicoapps.cooktime.ui.defaultExitAnimation

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    appNavGraphState: AppNavGraphState
) {
    AnimatedVisibility(
        modifier = modifier
            .windowInsetsPadding(BottomAppBarDefaults.windowInsets),
        visible = appNavGraphState.bottomBar.visible,
        label = "bottomAppBarAnimation",
        enter = defaultEnterAnimation(),
        exit = defaultExitAnimation()
    ) {
        BottomAppBar(
            modifier = modifier
                .animateContentSize(),
            actions = {
                appNavGraphState.bottomBar.actions?.invoke(this)
            },
            floatingActionButton = {
                appNavGraphState.bottomBar.floatingActionButton?.invoke()
            }
        )
    }
}
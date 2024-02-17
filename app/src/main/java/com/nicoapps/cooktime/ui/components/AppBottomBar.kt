package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.nicoapps.cooktime.ui.AppNavGraphState
import com.nicoapps.cooktime.ui.defaultAnimationSpec

@Composable
fun AppBottomBar(
    modifier: Modifier = Modifier,
    appNavGraphState: AppNavGraphState
) {
    AnimatedContent(
        modifier = modifier
            .windowInsetsPadding(BottomAppBarDefaults.windowInsets),
        targetState = appNavGraphState.bottomBar.visible,
        label = "bottomAppBarAnimation",
        transitionSpec = {
            if (!initialState && targetState) {
                slideInVertically(
                    animationSpec = defaultAnimationSpec()
                ) { height -> height } + fadeIn(
                    animationSpec = defaultAnimationSpec()
                ) togetherWith
                        slideOutVertically(
                            animationSpec = defaultAnimationSpec()
                        ) { height -> -height } + fadeOut(
                    animationSpec = defaultAnimationSpec()
                )
            } else {
                slideInVertically(
                    animationSpec = defaultAnimationSpec()
                ) { height -> -height } + fadeIn(
                    animationSpec = defaultAnimationSpec()
                ) togetherWith
                        slideOutVertically(
                            animationSpec = defaultAnimationSpec()
                        ) { height -> height } + fadeOut(
                    animationSpec = defaultAnimationSpec()
                )
            }.using(
                SizeTransform(clip = false)
            )
        }
    ) { showAppBottomBar ->
        if (showAppBottomBar) {
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
}
package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import com.nicoapps.cooktime.ui.defaultEnterAnimation
import com.nicoapps.cooktime.ui.defaultExitAnimation

@Composable
fun AppBottomBarFabContainer(
    floatingActionButton: (@Composable () -> Unit)? = null
) {
    AnimatedContent(
        targetState = floatingActionButton,
        label = "appBottomBarFabAnimation",
        transitionSpec = {
            if (initialState == null && targetState != null) {
                defaultEnterAnimation() togetherWith defaultExitAnimation()
            } else {
                defaultEnterAnimation() togetherWith defaultExitAnimation()
            }.using(
                SizeTransform(clip = true)
            )
        }
    ) { fab ->
        fab?.let {
            it()
        }
    }
}
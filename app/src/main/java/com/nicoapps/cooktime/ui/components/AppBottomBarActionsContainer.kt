package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.runtime.Composable
import com.nicoapps.cooktime.ui.defaultEnterAnimation
import com.nicoapps.cooktime.ui.defaultExitAnimation

@Composable
fun AppBottomBarActionsContainer(
    bottomBarActions: (@Composable RowScope.() -> Unit)? = null
) {
    AnimatedContent(
        targetState = bottomBarActions,
        label = "appBottomBarActionsAnimation",
        transitionSpec = {
            if (initialState == null && targetState != null) {
                defaultEnterAnimation() togetherWith defaultExitAnimation()
            } else {
                defaultEnterAnimation() togetherWith defaultExitAnimation()
            }.using(
                SizeTransform(clip = true)
            )
        }
    ) { actions ->
        actions?.let {
            Row {
                it(this)
            }
        }
    }
}
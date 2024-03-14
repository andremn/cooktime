package com.nicoapps.cooktime.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.SizeTransform
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.dimensionResource
import com.nicoapps.cooktime.R

fun <T> defaultAnimationSpec(durationMillis: Int = 350) = tween<T>(
    durationMillis = durationMillis
)

fun AnimatedContentTransitionScope<Boolean>.defaultTransitionSpec() =
    if (initialState && !targetState) {
        defaultEnterAnimation() togetherWith defaultExitAnimation(AnimationDirection.DOWNWARDS)
    } else {
        defaultEnterAnimation(AnimationDirection.DOWNWARDS) togetherWith defaultExitAnimation()
    }.using(
        SizeTransform(clip = true)
    )

fun defaultEnterAnimation(animationDirection: AnimationDirection = AnimationDirection.UPWARDS) =
    slideInVertically(
        animationSpec = defaultAnimationSpec()
    ) { height -> height * animationDirection.direction } + fadeIn(
        animationSpec = defaultAnimationSpec()
    )

fun defaultExitAnimation(animationDirection: AnimationDirection = AnimationDirection.UPWARDS) =
    slideOutVertically(
        animationSpec = defaultAnimationSpec()
    ) { height -> height * animationDirection.direction } + fadeOut(
        animationSpec = defaultAnimationSpec()
    )

@Composable
fun dialogSurfaceColor() =
    MaterialTheme.colorScheme.surfaceColorAtElevation(
        dimensionResource(id = R.dimen.dialog_elevation)
    )

@Composable
fun dialogTextFieldColors() =
    TextFieldDefaults.colors(
        focusedContainerColor = dialogSurfaceColor(),
        unfocusedContainerColor = dialogSurfaceColor(),
        errorContainerColor = dialogSurfaceColor()
    )

enum class AnimationDirection(val direction: Int) {
    DOWNWARDS(-1),
    UPWARDS(1)
}
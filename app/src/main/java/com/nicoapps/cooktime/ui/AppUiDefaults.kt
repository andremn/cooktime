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

const val DEFAULT_ANIMATION_DURATION_MILLIS = 350
const val DEFAULT_DELAY_MILLIS = 0

fun <T> defaultAnimationSpec(
    durationMillis: Int = DEFAULT_ANIMATION_DURATION_MILLIS,
    delayMillis: Int = DEFAULT_DELAY_MILLIS
) = tween<T>(
    durationMillis = durationMillis,
    delayMillis = delayMillis
)

fun AnimatedContentTransitionScope<Boolean>.defaultTransitionSpec() =
    if (initialState && !targetState) {
        defaultEnterAnimation() togetherWith defaultExitAnimation(AnimationDirection.DOWNWARDS)
    } else {
        defaultEnterAnimation(AnimationDirection.DOWNWARDS) togetherWith defaultExitAnimation()
    }.using(
        SizeTransform(clip = true)
    )

fun defaultEnterAnimation(
    animationDirection: AnimationDirection = AnimationDirection.UPWARDS,
    delayMillis: Int = DEFAULT_DELAY_MILLIS
) =
    slideInVertically(
        animationSpec = defaultAnimationSpec(delayMillis = delayMillis)
    ) { height -> height * animationDirection.direction } + fadeIn(
        animationSpec = defaultAnimationSpec(delayMillis = delayMillis)
    )

fun defaultExitAnimation(
    animationDirection: AnimationDirection = AnimationDirection.UPWARDS,
    delayMillis: Int = DEFAULT_DELAY_MILLIS
) =
    slideOutVertically(
        animationSpec = defaultAnimationSpec(delayMillis = delayMillis)
    ) { height -> height * animationDirection.direction } + fadeOut(
        animationSpec = defaultAnimationSpec(delayMillis = delayMillis)
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
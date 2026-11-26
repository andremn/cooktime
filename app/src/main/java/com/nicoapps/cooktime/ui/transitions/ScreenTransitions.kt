package com.nicoapps.cooktime.ui.transitions

import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clipToBounds
import androidx.navigation.NavBackStackEntry
import com.nicoapps.cooktime.ui.DEFAULT_ANIMATION_DURATION_MILLIS
import com.nicoapps.cooktime.ui.defaultAnimationSpec

/**
 * Visibility scope of the NavHost entry the current screen belongs to. Null outside the NavHost
 * (previews, isolated UI tests), where screens are simply not animated.
 */
val LocalNavAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * The entry below the current one on the NavHost's back stack, used to tell a forward navigation
 * from a return.
 */
val LocalPreviousNavBackStackEntry = compositionLocalOf<NavBackStackEntry?> { null }

/*
 * The NavHost itself doesn't move the screens (see AppNavHost): each part of a screen animates on
 * its own, so the bars stay in place while their content changes and the screen's content
 * slides, as when the app had a single shared scaffold. Screens draw no background of their own,
 * so both screens' content is visible while one slides over the other.
 */

/**
 * Keeps the incoming screen in place for as long as its parts animate. A NavHost transition with
 * no duration of its own would make a back navigation, which runs as a seekable transition,
 * jump straight to the destination.
 */
fun screenHoldEnterTransition(): EnterTransition =
    fadeIn(defaultAnimationSpec(durationMillis = SCREEN_HOLD_MILLIS), initialAlpha = 1f)

/** Keeps the outgoing screen in place for as long as its parts animate. */
fun screenHoldExitTransition(): ExitTransition =
    fadeOut(defaultAnimationSpec(durationMillis = SCREEN_HOLD_MILLIS), targetAlpha = 1f)

/** Long enough for the delayed floating action button to finish rising. */
private const val SCREEN_HOLD_MILLIS = DEFAULT_ANIMATION_DURATION_MILLIS * 2

/** The screen's content slides up into place and down out of the way, in both directions. */
fun Modifier.screenContentTransition(): Modifier = navAnimateEnterExit(
    enter = { slideInVertically(defaultAnimationSpec()) { height -> height } },
    exit = { slideOutVertically(defaultAnimationSpec()) { height -> height } }
)

/**
 * The top bar stays in place while its content changes: going forward, the new content comes
 * from above and the old one leaves downwards; going back, the other way around.
 */
fun Modifier.topBarTransition(): Modifier = clipToBounds().navAnimateEnterExit(
    enter = { isReturning ->
        slideInVertically(defaultAnimationSpec()) { height ->
            if (isReturning) height else -height
        } + fadeIn(defaultAnimationSpec())
    },
    exit = { isReturning ->
        slideOutVertically(defaultAnimationSpec()) { height ->
            if (isReturning) -height else height
        } + fadeOut(defaultAnimationSpec())
    }
)

/** The bottom bar's content rises into place and sinks out of the way. */
fun Modifier.bottomBarTransition(): Modifier = clipToBounds().navAnimateEnterExit(
    enter = { slideInVertically(defaultAnimationSpec()) { height -> height } + fadeIn() },
    exit = { slideOutVertically(defaultAnimationSpec()) { height -> height } + fadeOut() }
)

/** A floating action button rises from below the screen once the content is in place. */
fun Modifier.floatingActionButtonTransition(): Modifier = navAnimateEnterExit(
    enter = {
        slideInVertically(
            defaultAnimationSpec(delayMillis = DEFAULT_ANIMATION_DURATION_MILLIS)
        ) { height -> height * 3 }
    },
    exit = {
        slideOutVertically(defaultAnimationSpec()) { height -> height } +
                fadeOut(defaultAnimationSpec())
    }
)

private fun Modifier.navAnimateEnterExit(
    enter: (isReturning: Boolean) -> EnterTransition,
    exit: (isReturning: Boolean) -> ExitTransition
): Modifier = composed {
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current
    val previousEntry = LocalPreviousNavBackStackEntry.current

    if (animatedVisibilityScope == null) {
        Modifier
    } else {
        with(animatedVisibilityScope) {
            val isReturning = isReturning(previousEntry)

            Modifier.animateEnterExit(enter = enter(isReturning), exit = exit(isReturning))
        }
    }
}

/**
 * Whether the NavHost is going back. Going forward leaves the screen being left right below the
 * destination; a return pops it, so it's no longer there (back navigation completes before the
 * transition starts, since back isn't predictive inside the app).
 */
private fun AnimatedVisibilityScope.isReturning(previousEntry: NavBackStackEntry?): Boolean {
    val navTransition = transition.parentTransition ?: return false
    val initialEntry = navTransition.currentState as? NavBackStackEntry ?: return false
    val targetEntry = navTransition.targetState as? NavBackStackEntry ?: return false

    return initialEntry.id != targetEntry.id && initialEntry.id != previousEntry?.id
}

package com.nicoapps.cooktime.ui.transitions

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.RemeasureToBounds
import androidx.compose.animation.SharedTransitionScope.ResizeMode.Companion.scaleToBounds
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import com.nicoapps.cooktime.ui.ExecuteRecipe
import com.nicoapps.cooktime.ui.Home
import com.nicoapps.cooktime.ui.NewRecipe
import com.nicoapps.cooktime.ui.ViewRecipe
import kotlin.reflect.KClass

/**
 * Scope of the [androidx.compose.animation.SharedTransitionLayout] around the app's NavHost.
 * Null outside it (previews, isolated UI tests), where shared elements are simply not animated.
 */
val LocalSharedTransitionScope = staticCompositionLocalOf<SharedTransitionScope?> { null }

/** Visibility scope of the NavHost entry the current screen belongs to. */
val LocalNavAnimatedVisibilityScope = staticCompositionLocalOf<AnimatedVisibilityScope?> { null }

/**
 * Keys pairing an element on one screen with its counterpart on the next. Both sides must be
 * composed with an equal key for the transition to run; otherwise each side just follows its
 * screen's enter or exit transition.
 */
sealed interface SharedElementKey {
    /** A recipe card on the home screen and the whole recipe screen it opens. */
    data class RecipeContainer(val recipeId: Long) : SharedElementKey

    /** The recipe name on its card and as the recipe screen's title. */
    data class RecipeTitle(val recipeId: Long) : SharedElementKey

    /**
     * The start button on the recipe screen and the whole execution screen it opens, bars
     * included, so the screen visibly grows out of the button.
     */
    data class ExecuteRecipeContainer(val recipeId: Long) : SharedElementKey

    /** The home screen's new recipe button and the whole new recipe screen it opens. */
    data object NewRecipeContainer : SharedElementKey
}

/**
 * A container transform: the element grows into (or shrinks back from) the element with the
 * same [key] on the other screen while the contents of both sides cross-fade. Each side keeps
 * the layout of its final size and is revealed by the animated bounds, so text never re-wraps
 * mid-transition. The cross-fade is short, so the container turns opaque early instead of
 * showing the screen underneath through it.
 */
fun Modifier.sharedContainer(
    key: SharedElementKey,
    clipShape: Shape = RectangleShape
): Modifier = sharedBounds(
    key,
    clipShape,
    resizeMode = RemeasureToBounds,
    keepFinalLayout = true,
    contentFadeMillis = CONTAINER_CONTENT_FADE_MILLIS,
    zIndexInOverlay = 0f
)

/**
 * An element that stays in place, or moves, between two screens, cross-fading its content.
 * Drawn above any [sharedContainer] it is nested in.
 */
fun Modifier.sharedElement(key: SharedElementKey): Modifier = sharedBounds(
    key,
    RectangleShape,
    resizeMode = scaleToBounds(),
    keepFinalLayout = false,
    contentFadeMillis = TRANSITION_DURATION_MILLIS,
    zIndexInOverlay = 1f
)

private fun Modifier.sharedBounds(
    key: SharedElementKey,
    clipShape: Shape,
    resizeMode: SharedTransitionScope.ResizeMode,
    keepFinalLayout: Boolean,
    contentFadeMillis: Int,
    zIndexInOverlay: Float
): Modifier = composed {
    val sharedTransitionScope = LocalSharedTransitionScope.current
    val animatedVisibilityScope = LocalNavAnimatedVisibilityScope.current

    if (sharedTransitionScope == null || animatedVisibilityScope == null) {
        Modifier
    } else {
        with(sharedTransitionScope) {
            Modifier.sharedBounds(
                sharedContentState = rememberSharedContentState(key),
                animatedVisibilityScope = animatedVisibilityScope,
                enter = fadeIn(tween(contentFadeMillis)),
                exit = fadeOut(tween(contentFadeMillis)),
                resizeMode = resizeMode,
                clipInOverlayDuringTransition = OverlayClip(clipShape),
                zIndexInOverlay = zIndexInOverlay
            ).let { if (keepFinalLayout) it.skipToLookaheadSize() else it }
        }
    }
}

private const val TRANSITION_DURATION_MILLIS = 300
private const val FADE_OUT_DURATION_MILLIS = 90
private const val CONTAINER_CONTENT_FADE_MILLIS = 100
private const val FADE_THROUGH_INITIAL_SCALE = 0.92f

/**
 * Destinations linked by a container transform. Between them the shared elements do the
 * motion: the incoming screen appears at once, since its container already reveals it (fading
 * it in would make the container see-through), and the outgoing one fades out.
 */
private val containerTransformPairs = setOf(
    Home::class to ViewRecipe::class,
    Home::class to NewRecipe::class,
    ViewRecipe::class to ExecuteRecipe::class
)

private fun AnimatedContentTransitionScope<NavBackStackEntry>.isContainerTransform(): Boolean {
    val initialDestination = initialState.destination
    val targetDestination = targetState.destination

    return containerTransformPairs.any { (first, second) ->
        (initialDestination.hasDestinationRoute(first) && targetDestination.hasDestinationRoute(second)) ||
                (initialDestination.hasDestinationRoute(second) && targetDestination.hasDestinationRoute(first))
    }
}

/** [NavDestination.hasRoute] only accepts [KClass] values of a known type. */
@Suppress("UNCHECKED_CAST")
private fun NavDestination.hasDestinationRoute(route: KClass<*>): Boolean =
    hasRoute(route as KClass<Any>)

/** Container transform between linked screens, Material's fade through between the others. */
fun AnimatedContentTransitionScope<NavBackStackEntry>.screenEnterTransition(): EnterTransition =
    if (isContainerTransform()) {
        EnterTransition.None
    } else {
        val incomingSpec = tween<Float>(
            durationMillis = TRANSITION_DURATION_MILLIS - FADE_OUT_DURATION_MILLIS,
            delayMillis = FADE_OUT_DURATION_MILLIS
        )
        fadeIn(incomingSpec) + scaleIn(incomingSpec, initialScale = FADE_THROUGH_INITIAL_SCALE)
    }

fun AnimatedContentTransitionScope<NavBackStackEntry>.screenExitTransition(): ExitTransition =
    if (isContainerTransform()) {
        fadeOut(tween(TRANSITION_DURATION_MILLIS))
    } else {
        fadeOut(tween(FADE_OUT_DURATION_MILLIS))
    }

package com.nicoapps.cooktime.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import com.nicoapps.cooktime.ui.ExecuteRecipe
import com.nicoapps.cooktime.ui.Home
import com.nicoapps.cooktime.ui.NewRecipe
import com.nicoapps.cooktime.ui.Settings
import com.nicoapps.cooktime.ui.ViewRecipe
import com.nicoapps.cooktime.ui.screens.recipe.create.NewRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.edit.ViewRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.execute.ExecuteRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.list.HomeScreen
import com.nicoapps.cooktime.ui.screens.settings.SettingsScreen
import com.nicoapps.cooktime.ui.transitions.LocalNavAnimatedVisibilityScope
import com.nicoapps.cooktime.ui.transitions.LocalPreviousNavBackStackEntry
import com.nicoapps.cooktime.ui.transitions.screenHoldEnterTransition
import com.nicoapps.cooktime.ui.transitions.screenHoldExitTransition

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appSnackbarState: AppSnackbarState,
    onOpenDrawer: () -> Unit
) {
    val currentEntry by navController.currentBackStackEntryAsState()
    val previousEntry = remember(currentEntry) { navController.previousBackStackEntry }

    // The screens animate their parts themselves (see ScreenTransitions), so the NavHost doesn't
    // move them: it only holds both screens for as long as those animations run.
    CompositionLocalProvider(LocalPreviousNavBackStackEntry provides previousEntry) {
        NavHost(
            modifier = modifier,
            navController = navController,
            startDestination = Home,
            enterTransition = { screenHoldEnterTransition() },
            exitTransition = { screenHoldExitTransition() },
            popEnterTransition = { screenHoldEnterTransition() },
            popExitTransition = { screenHoldExitTransition() }
        ) {
            screen<Home> { backStackEntry ->
                HomeScreen(
                    appSnackbarState = appSnackbarState,
                    onOpenDrawer = onOpenDrawer,
                    onRecipeClick = { recipeId, recipeTitle ->
                        backStackEntry.ifResumed {
                            navController.navigate(ViewRecipe(recipeId, recipeTitle)) {
                                launchSingleTop = true
                            }
                        }
                    },
                    onNewRecipeClick = {
                        backStackEntry.ifResumed {
                            navController.navigate(NewRecipe) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            screen<Settings> {
                SettingsScreen()
            }

            screen<NewRecipe> {
                NewRecipeScreen(
                    appSnackbarState = appSnackbarState,
                    onRecipeSaved = {
                        navController.navigate(Home) {
                            popUpTo<Home>()
                            launchSingleTop = true
                        }
                    }
                )
            }

            screen<ViewRecipe> { backStackEntry ->
                ViewRecipeScreen(
                    snackbarState = appSnackbarState,
                    onRecipeDeleted = {
                        navController.popBackStackIfCurrent<ViewRecipe>()
                    },
                    onStartExecution = { recipeId, recipeTitle ->
                        backStackEntry.ifResumed {
                            navController.navigate(ExecuteRecipe(recipeId, recipeTitle)) {
                                launchSingleTop = true
                            }
                        }
                    }
                )
            }

            screen<ExecuteRecipe> {
                ExecuteRecipeScreen(
                    onFinished = {
                        navController.popBackStackIfCurrent<ExecuteRecipe>()
                    }
                )
            }
        }
    }

    // Registered after the NavHost's own predictive back handler, so it takes precedence: the
    // screens don't follow the gesture, and the transition only runs once the gesture completes.
    // Back handlers the screens register later (editing, new recipe steps, the execution's save
    // dialog) still come first, and on the start destination the system handles back.
    BackHandler(enabled = previousEntry != null) {
        navController.popBackStack()
    }
}

/**
 * Runs [block] only while this entry is the resumed screen, so a double tap or an event repeated
 * during a transition doesn't stack the same screen twice.
 */
private inline fun NavBackStackEntry.ifResumed(block: () -> Unit) {
    if (lifecycle.currentState.isAtLeast(Lifecycle.State.RESUMED)) {
        block()
    }
}

/**
 * Pops the back stack only when this destination is the one on top, so a programmatic return that
 * runs more than once doesn't pop a second screen.
 */
private inline fun <reified T : Any> NavHostController.popBackStackIfCurrent() {
    if (currentDestination?.hasRoute<T>() == true) {
        popBackStack()
    }
}

/**
 * Registers a destination written as a typed route, giving its content the scope its parts animate
 * in (see ScreenTransitions).
 */
private inline fun <reified T : Any> NavGraphBuilder.screen(
    noinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T> { backStackEntry ->
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            content(backStackEntry)
        }
    }
}

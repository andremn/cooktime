package com.nicoapps.cooktime.ui.components

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.nicoapps.cooktime.ui.AllDestinations
import com.nicoapps.cooktime.ui.AppNavigationActions
import com.nicoapps.cooktime.ui.screens.recipe.create.NewRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.edit.ViewRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.execute.ExecuteRecipeScreen
import com.nicoapps.cooktime.ui.screens.recipe.list.HomeScreen
import com.nicoapps.cooktime.ui.screens.settings.SettingsScreen
import com.nicoapps.cooktime.ui.transitions.LocalNavAnimatedVisibilityScope
import com.nicoapps.cooktime.ui.transitions.LocalSharedTransitionScope
import com.nicoapps.cooktime.ui.transitions.screenEnterTransition
import com.nicoapps.cooktime.ui.transitions.screenExitTransition

@Composable
fun AppNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    appSnackbarState: AppSnackbarState,
    navigationActions: AppNavigationActions,
    onOpenDrawer: () -> Unit
) {
    SharedTransitionLayout(modifier = modifier) {
        CompositionLocalProvider(LocalSharedTransitionScope provides this) {
            NavHost(
                navController = navController,
                startDestination = AllDestinations.HOME,
                enterTransition = { screenEnterTransition() },
                exitTransition = { screenExitTransition() },
                popEnterTransition = { screenEnterTransition() },
                popExitTransition = { screenExitTransition() }
            ) {
                screen(AllDestinations.HOME) {
                    HomeScreen(
                        appNavigationActions = navigationActions,
                        appSnackbarState = appSnackbarState,
                        onOpenDrawer = onOpenDrawer
                    )
                }

                screen(AllDestinations.SETTINGS) {
                    SettingsScreen()
                }

                screen(AllDestinations.NEW_RECIPE) {
                    NewRecipeScreen(
                        appSnackbarState = appSnackbarState,
                        appNavigationActions = navigationActions
                    )
                }

                screen(
                    route = "${AllDestinations.VIEW_RECIPE}/{recipeId}?recipeTitle={recipeTitle}",
                    arguments = listOf(
                        navArgument("recipeId") { type = NavType.LongType },
                        recipeTitleArgument()
                    )
                ) {
                    ViewRecipeScreen(
                        snackbarState = appSnackbarState,
                        appNavigationActions = navigationActions
                    )
                }

                screen(
                    route = "${AllDestinations.EXECUTE_RECIPE}/{recipeId}?recipeTitle={recipeTitle}",
                    arguments = listOf(
                        navArgument("recipeId") { type = NavType.LongType },
                        recipeTitleArgument()
                    )
                ) {
                    ExecuteRecipeScreen(
                        appNavigationActions = navigationActions
                    )
                }
            }
        }
    }
}

/**
 * The recipe name known when navigating, so the destination can show it, and animate it as a
 * shared element, before the recipe loads.
 */
private fun recipeTitleArgument() = navArgument("recipeTitle") {
    type = NavType.StringType
    defaultValue = ""
}

/** A destination whose content can take part in shared element transitions. */
private fun NavGraphBuilder.screen(
    route: String,
    arguments: List<NamedNavArgument> = emptyList(),
    content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable(route = route, arguments = arguments) { backStackEntry ->
        CompositionLocalProvider(LocalNavAnimatedVisibilityScope provides this) {
            content(backStackEntry)
        }
    }
}
